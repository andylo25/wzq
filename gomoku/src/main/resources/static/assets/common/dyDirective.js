/*!
 * Created By:xihun;
 * Created Time:2016-11-07;
 * 网站公用指令
 */
var dyDir = angular.module("dyDir", ["dyService"])
    .directive("dyValicode", function(scopeService) { //图形验证码
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                scope.valicode = '/common/public/getImageCode/' + new Date().getTime();
                element.click(function(){
                    scopeService.safeApply(scope, function() {
                        scope.valicode = '/common/public/getImageCode/' + new Date().getTime();
                    });
                });
                
            }
        }
    })
    //复选框指令
    .directive('checklistModel', ['$parse', '$compile', function ($parse, $compile) {
        // contains
        function contains(arr, item, comparator) {
            if (angular.isArray(arr)) {
                for (var i = arr.length; i--;) {
                    if (comparator(arr[i], item)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // add
        function add(arr, item, comparator) {
            arr = angular.isArray(arr) ? arr : [];
            if (!contains(arr, item, comparator)) {
                arr.push(item);
            }
            return arr;
        }

        // remove
        function remove(arr, item, comparator) {
            if (angular.isArray(arr)) {
                for (var i = arr.length; i--;) {
                    if (comparator(arr[i], item)) {
                        arr.splice(i, 1);
                        break;
                    }
                }
            }
            return arr;
        }

        // http://stackoverflow.com/a/19228302/1458162
        function postLinkFn(scope, elem, attrs) {
            // exclude recursion, but still keep the model
            var checklistModel = attrs.checklistModel;
            attrs.$set("checklistModel", null);
            // compile with `ng-model` pointing to `checked`
            $compile(elem)(scope);
            attrs.$set("checklistModel", checklistModel);

            // getter / setter for original model
            var getter = $parse(checklistModel);
            var setter = getter.assign;
            var checklistChange = $parse(attrs.checklistChange);
            var checklistBeforeChange = $parse(attrs.checklistBeforeChange);

            // value added to list
            var value = attrs.checklistValue ? $parse(attrs.checklistValue)(scope.$parent) : attrs.value;


            var comparator = angular.equals;

            if (attrs.hasOwnProperty('checklistComparator')) {
                if (attrs.checklistComparator[0] == '.') {
                    var comparatorExpression = attrs.checklistComparator.substring(1);
                    comparator = function (a, b) {
                        return a[comparatorExpression] === b[comparatorExpression];
                    };

                } else {
                    comparator = $parse(attrs.checklistComparator)(scope.$parent);
                }
            }

            // watch UI checked change
            scope.$watch(attrs.ngModel, function (newValue, oldValue) {
                if (newValue === oldValue) {
                    return;
                }

                if (checklistBeforeChange && (checklistBeforeChange(scope) === false)) {
                    scope[attrs.ngModel] = contains(getter(scope.$parent), value, comparator);
                    return;
                }

                setValueInChecklistModel(value, newValue);

                if (checklistChange) {
                    checklistChange(scope);
                }
            });

            function setValueInChecklistModel(value, checked) {
                var current = getter(scope.$parent);
                if (angular.isFunction(setter)) {
                    if (checked === true) {
                        setter(scope.$parent, add(current, value, comparator));
                    } else {
                        setter(scope.$parent, remove(current, value, comparator));
                    }
                }

            }

            // declare one function to be used for both $watch functions
            function setChecked(newArr, oldArr) {
                if (checklistBeforeChange && (checklistBeforeChange(scope) === false)) {
                    setValueInChecklistModel(value, scope[attrs.ngModel]);
                    return;
                }
                scope[attrs.ngModel] = contains(newArr, value, comparator);
            }

            // watch original model change
            // use the faster $watchCollection method if it's available
            if (angular.isFunction(scope.$parent.$watchCollection)) {
                scope.$parent.$watchCollection(checklistModel, setChecked);
            } else {
                scope.$parent.$watch(checklistModel, setChecked, true);
            }
        }

        return {
            restrict: 'A',
            priority: 1000,
            terminal: true,
            scope: true,
            compile: function (tElement, tAttrs) {
                if ((tElement[0].tagName !== 'INPUT' || tAttrs.type !== 'checkbox') && (tElement[0].tagName !== 'MD-CHECKBOX') && (!tAttrs.btnCheckbox)) {
                    throw 'checklist-model should be applied to `input[type="checkbox"]` or `md-checkbox`.';
                }

                if (!tAttrs.checklistValue && !tAttrs.value) {
                    throw 'You should provide `value` or `checklist-value`.';
                }

                // by default ngModel is 'checked', so we set it if not specified
                if (!tAttrs.ngModel) {
                    // local scope var storing individual checkbox model
                    tAttrs.$set("ngModel", "checked");
                }

                return postLinkFn;
            }
        };
    }])
    //处理select联动，如省市联动等
    .directive("dySelect", function($http) { 
        return {
            require: '?ngModel',
            //scope:{
                //'dyParams':'@?'
            //},
            restrict: 'ACE',
            link: function(scope, element, attrs) {
                var url = attrs.dataurl,//联动的地址
                    params = attrs.dataparams ? eval('('+attrs.dataparams+')') : "",
                    len = params != "" ? params.length : 0;
                scope.linkData = {}; //存放请求回来的数据
                $http.post(url).success(function(data){
                    scope.linkData[0] = data.data;
                });
                //console.log(params[0]);
                scope.linkFn = function(params){
                    if((params.index+1)==scope.list.linklist.length){
                        return;
                    }
                    $http({
                        method: 'post',
                        url: url,
                        data: $.param(params),
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        }
                    }).success(function(data) {
                        scope.linkData[params.index+1] = data.data;
                    });
                }

                //初始化选择省-市-区  dataparams="[{pid: 值, index: 0},{pid: 值, index: 1},{pid: 值, index: 2}]"
                if(len > 0){
                    for(var key=0; key<len; key++){
                        scope.linkFn(params[key]);
                    }
                }

                /*for(var i=0;i<len-1;i++){
                    console.log(i);*/
                // if(len==2){  //初始化选择省-市-区  dataparams="['省字段', '市字段', '县字段']"
                //     scope.$watch('formData[params[0]]',function(newValue,oldValue){
                //         var linkparams = {
                //             'index':0
                //         };
                //         linkparams[params[0]] = scope.formData[params[0]];
                //         scope.linkFn(linkparams);
                //     });
                // }else if(len==3){
                //     scope.$watch('formData[params[0]]',function(newValue,oldValue){
                //         var linkparams = {
                //             'index':0
                //         };
                //         linkparams[params[0]] = scope.formData[params[0]];
                //         scope.linkFn(linkparams);
                //     });
                //     scope.$watch('formData[params[1]]',function(newValue,oldValue){
                //         var linkparams = {
                //             'index':1
                //         };
                //         linkparams[params[1]] = scope.formData[params[1]];
                //         scope.linkFn(linkparams);
                //     });
                // }
                /*}*/
                    
                
                
            }
        }
    })
    //日历
    .directive("datePicker", function ($injector) {
        return {
            require: '?ngModel',
            link: function (scope, element, attrs, ngModel) {
                if(typeof WdatePicker=='undefined'){
                    window.WdatePickerUrl = '/assets/src/js/plugins/datepicker/';
                    var script=document.createElement('script'); 
                    script.src= WdatePickerUrl+'WdatePicker.js'; 
                    script.type='text/javascript'; 
                    script.defer=true; 
                    $('body').eq(0).append(script);
                }
                var defaults, options;
                scope.deVal = element.attr('data-default-val') ? element.attr('data-default-val') : '';
                ngModel.$render = function () {
                    element.val(ngModel.$viewValue || scope.deVal);
                };
                var typeArr = new Array('start_time', 'end_time');
                // Write data to the model
                function read() {
                    var val = element.val();
                    ngModel.$setViewValue(val);
                }

                function contains(arr, obj) {
                    var i = arr.length;
                    while (i--) {
                        if (arr[i] === obj) {
                            return true;
                        }
                    }
                    return false;
                }

                function setPicker(than) {
                    if (!contains(typeArr, than)) {
                        options = angular.extend({}, {
                            el: attrs.id
                        }, options);
                    }
                    WdatePicker(options);
                }

                defaults = {
                    readOnly: true,
                    dateFmt: 'yyyy-MM-dd'
                };

                switch (attrs.id) {
                    case 'start_time':
                        options = {
                            maxDate: '#F{$dp.$D(\'end_time\')}',
                            el: 'start_time'
                        };
                        break;
                    case 'end_time':
                        options = {
                            minDate: '#F{$dp.$D(\'start_time\')}',
                            el: 'end_time'
                        };
                        break;
                }
                options = angular.extend({}, options, defaults, scope.$eval(attrs.datePicker));
                element.on("focus click", function () {
                    setPicker(attrs.id);
                    scope.$apply(read);
                });
                $('.date-picker-icon').on('click', function () {
                    setPicker($(this).prev().attr('id'));
                });
            }
        }
    })
    //双日历时间范围
    .directive("dateRangePicker", function($timeout){
        return {
            restrict: "EA",
            scope: {
                filterDate: '&'  //列表选完日期后筛选数据的回调事件
            },
            require: "?ngModel",
            link: function(scope, element, attr, ctrl){
                if(typeof dateRangePickerUrl == "undefined"){
                    window.dateRangePickerUrl = "/assets/src/js/plugins/dateRangePicker/";

                    var style1 = document.createElement("link");
                    style1.href = dateRangePickerUrl + "daterangepicker.css";
                    style1.rel = "stylesheet";
                    style1.type = "text/css";
                    $("body").eq(0).append(style1);

                    var script1 = document.createElement("script");
                    script1.src = dateRangePickerUrl + "moment.min.js";
                    script1.type ="text/javascript";
                    script1.defer = true;
                    $("body").eq(0).append(script1);

                    var script2 = document.createElement("script");
                    script2.src = dateRangePickerUrl + "jquery.daterangepicker.js";
                    script2.type ="text/javascript";
                    script2.defer = true;
                    $("body").eq(0).append(script2);
                }

                var defaults = {
                    format: "YYYY.MM.DD",
                    separator: "-"
                },
                options = angular.extend({}, defaults, scope.$eval(attr.pickerOptions));
                $timeout(function(){
                    $("#" + attr.id).dateRangePicker(options).bind('datepicker-apply',function(){
                        scope.$apply(function() {
                            ctrl.$setViewValue($("#" + attr.id).val());
                        });
                    }).bind('datepicker-clear',function(){
                        scope.$apply(function() {
                            $("#" + attr.id).val("");
                            ctrl.$setViewValue("");  //清除选中日期
                        });
                        scope.filterDate();  //列表选完日期后筛选数据的回调事件
                    }).bind('datepicker-closed',function(){
                        scope.filterDate();  //列表选完日期后筛选数据的回调事件
                    })
                }, 500);
            }
        }
    })
    //表单里的文章编辑器
    .directive('ueditor', function ($timeout) {
        return {
            require: '?ngModel',
            scope: {},
            link: function ($S, element, attr, ctrl) {
                if(typeof UM === 'undefined') {
                    window.UMEDITOR_HOME_URL = '/assets/src/js/plugins/umeditor/'; //配置编辑器的路径，建议用绝对路径
                    var style1 = document.createElement('link');
                    style1.href = UMEDITOR_HOME_URL+'themes/default/css/umeditor.css';
                    style1.rel = 'stylesheet';
                    style1.type = 'text/css';
                     $('head').eq(0).append(style1);
                    
                    var script1 = document.createElement('script'); 
                    script1.src = UMEDITOR_HOME_URL+'umeditor.config.js'; 
                    script1.type = 'text/javascript'; 
                    script1.defer = true; 
                    $('head').eq(0).append(script1);
                    var script2 = document.createElement('script'); 
                    script2.src = UMEDITOR_HOME_URL+'umeditor.min.js'; 
                    script2.type = 'text/javascript'; 
                    script2.defer = true; 
                    $('head').eq(0).append(script2);
                    //window.UMEDITOR_CONFIG.UMEDITOR_HOME_URL = 'assets/src/js/plugins/umeditor/';
                }
                var _NGUeditor, _updateByRender;
                _updateByRender = false;
                _NGUeditor = (function () {
                    function _NGUeditor() {
                        this.bindRender();
                        this.initEditor();
                        return;
                    }

                    /**
                     * 初始化编辑器
                     * @return {[type]} [description]
                     */

                    _NGUeditor.prototype.initEditor = function () {
                        var _UEConfig, _editorId,
                            _self;
                        _self = this;
                        if (typeof UM === 'undefined') {
                            console.error("Please import the local resources of ueditor!");
                            return;
                        }
                        //_editor+name+[10000000~99999999]随机数
                        _editorId = attr.id ? attr.id : "_editor_" + element[0].name + parseInt(Math.random() * 90000000 + 10000000, 10);
                        element[0].id = _editorId;
                        this.editor = UM.getEditor(_editorId);
                        return this.editor.ready(function () {
                            _self.editorReady = true;
                            _self.editor.addListener("contentChange", function () {
                                ctrl.$setViewValue(_self.editor.getContent());
                                if (!_updateByRender) {
                                    if (!$S.$$phase) {
                                        $S.$apply();
                                    }
                                }
                                _updateByRender = false;
                            });

                            _self.editor.addListener('fullscreenchanged', function (type, isfullscreen) {
                                if (!isfullscreen) {
                                    // 重置编辑器内容区域高度，这个高度在切换全屏时会改变，应该是 UMEditor 自身的BUG
                                    $("body").css({"overflow-y": "scroll"});

                                    // 重新布局外部容器
                                    //_self.updateLayout();
                                }
                            });
                            if (_self.modelContent.length > 0) {
                                _self.setEditorContent();
                            }
                            if (typeof $S.ready === "function") {
                                $S.ready(_self.editor);
                            }
                        });
                    };

                    _NGUeditor.prototype.setEditorContent = function (content) {
                        if (content == null) {
                            content = this.modelContent;
                        }
                        if (this.editor && this.editorReady) {
                            this.editor.setContent(content);
                        }
                    };

                    _NGUeditor.prototype.bindRender = function () {
                        var _self;
                        _self = this;
                        ctrl.$render = function () {
                            _self.modelContent = (ctrl.$isEmpty(ctrl.$viewValue) ? "" : ctrl.$viewValue);
                            _updateByRender = true;
                            _self.setEditorContent();
                        };
                    };

                    return _NGUeditor;

                })();
                new _NGUeditor();
            }
        };
    })
    .directive("ngFileUpload", function(FileUploader){
        return {
            restrict: "A",
            require: "?ngModel",
            templateUrl: "/assets/ngtpl/upload.html",
            scope: {
                "ngFileUpload": "="
            },
            link: function(scope, element, attrs, ngModel){
                // scope.uploader = scope.ngFileUpload.uploader;
                scope.sumData = scope.ngFileUpload.sumData;
                scope.names = scope.ngFileUpload.name;


                scope.type = scope.ngFileUpload.type || "images";
                scope.required = scope.ngFileUpload.required || "1";
                scope.multiple = scope.ngFileUpload.multiple || "1";

                scope.random = Math.floor(Math.random()*10000000);

                scope.uploader = new FileUploader({
                    url: "/common/fileupload/file/picture",
                    autoUpload: true,
                    // queueLimit: 1,
                    alias: "upload"
                });

                scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {
                    if(response.status == 200){
                        scope.$apply(function () {
                            if(scope.multiple == "1"){
                                scope.sumData[scope.names] = scope.ngFileUpload.sumData[scope.names] ? scope.ngFileUpload.sumData[scope.names] : [];
                                scope.sumData[scope.names].push({
                                    file_url: response.data.Filedata.file_url,
                                    url: response.data.Filedata.url,
                                    name: response.data.Filedata.name,
                                    did: response.data.did,
                                    size: response.data.Filedata.size,
                                    time: response.data.Filedata.time,
                                    format: response.data.Filedata.format
                                });
                            } else {
                                scope.sumData[scope.names] = scope.ngFileUpload.sumData[scope.names] ? scope.ngFileUpload.sumData[scope.names] : {};
                                scope.sumData[scope.names] = {
                                    file_url: response.data.Filedata.file_url,
                                    url: response.data.Filedata.url,
                                    name: response.data.Filedata.name,
                                    did: response.data.did,
                                    size: response.data.Filedata.size,
                                    time: response.data.Filedata.time,
                                    format: response.data.Filedata.format
                                }
                            }
                        });
                    } else {
                        layer.msg(response.description, {icon: 2, time: 2000, shade: 0.3})
                    }
                };

                scope.clearQueue = function(){
                    scope.uploader.clearQueue();
                }

                scope.removeFile = function(index){
                    if(scope.multiple == "1"){
                        scope.sumData[scope.names].splice(index, 1);
                    } else {
                        scope.sumData[scope.names] = {};
                    }
                }
            }
        }
    })
    //图片懒加载
    .directive("lazy", function ($timeout) {
        return {
            restrict: 'AC',
            scope: {},
            link: function (scope, element, attrs) {
                function test(time) {
                    $timeout(function () {
                        element.lazyload({
                            effect: 'fadeIn',
                            effectspeed: 200,
                            skip_invisible: false
                        });
                    }, time);
                }

                scope.$watch(function () {
                    return element.attr('data-original');
                }, function () {
                    test(0);
                }, true);
            }
        };
    })
    //上传图片
    .directive('uploadFile', function ($timeout) {
        return {
            require: '?ngModel',
            scope: {},
            link: function ($S, element, attr, ctrl) {
                if(typeof WebUploader==='undefined'){
                    var style3 = document.createElement('link');
                    style3.href = '/assets/src/js/plugins/webuploader/webuploader.css';
                    style3.rel = 'stylesheet';
                    style3.type = 'text/css';
                     $('head').eq(0).append(style3);
                    var script3=document.createElement('script'); 
                    script3.src='/assets/src/js/plugins/webuploader/webuploader.min.js'; 
                    script3.type='text/javascript'; 
                    script3.defer=true; 
                    $('head').eq(0).append(script3);
                }
                var $this = element,
                    // 优化retina, 在retina下这个值是2
                    ratio = window.devicePixelRatio || 1,
                    // 缩略图大小
                    thumbnailWidth = 100 * ratio,
                    thumbnailHeight = 100 * ratio,
                    $list = $this.siblings('.uploader-list');
                var uploader = WebUploader.create({

                    // 自动上传。
                    auto: false,

                    // swf文件路径
                    // swf: BASE_URL + '/js/Uploader.swf',
                    swf: 'Uploader.swf',

                    // 文件接收服务端。
                    //server: 'http://webuploader.duapp.com/server/fileupload.php',
                    server: 'http://2betop.net/fileupload.php',

                    // 选择文件的按钮。可选。
                    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                    // pick: '#filePicker',
                    pick: $this,

                    // 只允许选择文件，可选。
                    accept: {
                        title: 'Images',
                        extensions: 'gif,jpg,jpeg,bmp,png',
                        mimeTypes: 'image/*'
                    }
                });
                // 当有文件添加进来的时候
                uploader.on( 'fileQueued', function( file ) {
                    var $li = $(
                            '<div id="' + file.id + '" class="file-item thumbnail">' +
                                '<img>' +
                                '<div class="info">' + file.name + '</div>' +
                            '</div>'
                            ),
                    $img = $li.find('img');
                    $list.append( $li );

                    // 创建缩略图
                    uploader.makeThumb( file, function( error, src ) {
                        if ( error ) {
                            $img.replaceWith('<span>不能预览</span>');
                            return;
                        }

                        $img.attr( 'src', src );
                    }, thumbnailWidth, thumbnailHeight );
                });

                // 文件上传过程中创建进度条实时显示。
                uploader.on( 'uploadProgress', function( file, percentage ) {
                    var $li = $( '#'+file.id ),
                        $percent = $li.find('.progress span');

                    // 避免重复创建
                    if ( !$percent.length ) {
                        $percent = $('<p class="progress"><span></span></p>')
                                .appendTo( $li )
                                .find('span');
                    }

                    $percent.css( 'width', percentage * 100 + '%' );
                });

                // 文件上传成功，给item添加成功class, 用样式标记上传成功。
                uploader.on( 'uploadSuccess', function( file ) {
                    $( '#'+file.id ).addClass('upload-state-done');
                });

                // 文件上传失败，现实上传出错。
                uploader.on( 'uploadError', function( file ) {
                    var $li = $( '#'+file.id ),
                        $error = $li.find('div.error');

                    // 避免重复创建
                    if ( !$error.length ) {
                        $error = $('<div class="error"></div>').appendTo( $li );
                    }

                    $error.text('上传失败');
                });

                // 完成上传完了，成功或者失败，先删除进度条。
                uploader.on( 'uploadComplete', function( file ) {
                    $( '#'+file.id ).find('.progress').remove();
                });
            }
        };
    })
    //图表Echarts
    .directive("dyEcharts", function () {
        return {
            scope: {
                dyEcharts: '=?'
            },
            link: function (scope, element, iAttrs, ctrl) {
                var _chartsId = iAttrs.id ? iAttrs.id : "_echarts" + (Date.now());
                element[0].id = _chartsId;
                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: ''
                    },
                    tooltip: {},
                    legend: {},
                    series: []
                };

                // 使用刚指定的配置项和数据显示图表。
                scope.$watch('dyEcharts', function (newVal, oldVal) {
                    // 基于准备好的dom，初始化echarts实例
                    myChart = echarts.init(document.getElementById(_chartsId));
                    myChart.showLoading();
                    if (newVal) {
                        var optionConfig = angular.extend({}, option, newVal);
                        myChart.setOption(optionConfig);
                        myChart.hideLoading();
                    }

                });

            }
        }
    })
    //添加
    .directive("dyAdd", function($http){
        return {
            require: "?ngModel",
            scope: {
                "dyAdd": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.dyAdd;
                    parent.layer.open({
                        type: 2,
                        title: params.title,
                        shadeClose: false,
                        maxmin: true,
                        shade: 0.3,
                        area: ["750px", "650px"],
                        content: "/" + params.url //iframe的url
                    });
                })
            }
        }
    })
    //添加-带参数
    .directive("commonAdd", function($http){
        return {
            require: "?ngModel",
            scope: {
                "commonAdd": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.commonAdd;
                    var url = "/" + params.url + "?",
                        key;
                    for(key in params.form_data){
                        url += key + "=" + params.form_data[key] + "&"
                    }
                    url = url.substring(0, url.length-1);
                    parent.layer.open({
                        type: 2,
                        title: params.title,
                        shadeClose: false,
                        maxmin: true,
                        shade: 0.3,
                        area: ["750px", "650px"],
                        content: url //iframe的url
                    });
                })
            }
        }
    })
    //编辑
    .directive("dyEdit", function($http, postUrl){
        return {
            require: "?ngModel",
            scope: {
                "dyEdit": "=",
                "checkValue": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.dyEdit;
                    params.title = params.title || "编辑";
                    if(scope.checkValue.length > 1 && params.single == 1){
                        parent.layer.msg(params.title + "的选项只能为一项", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        })
                    } else if (scope.checkValue.length == 0){
                        parent.layer.msg("请先选择要" + params.title + "的选项！", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        })
                    } else {
                        var checkValue = scope.checkValue.join(",");
                        if(params.click == "900"){
                            parent.layer.open({
                                type: 2,
                                title: params.title,
                                shadeClose: false,
                                maxmin: true,
                                shade: 0.3,
                                area: ["900px", "650px"],
                                content: "/" + params.url + "?id=" + checkValue //编辑的页面地址，需要传递id给后台
                            });
                        } else {
                            parent.layer.open({
                                type: 2,
                                title: params.title,
                                shadeClose: false,
                                maxmin: true,
                                shade: 0.3,
                                area: ["750px", "650px"],
                                content: "/" + params.url + "?id=" + checkValue //编辑的页面地址，需要传递id给后台
                            });
                        }
                    }
                })
            }
        }
    })
    //编辑-带参数
    .directive("commonEdit", function($http){
        return {
            require: "?ngModel",
            scope: {
                "commonEdit": "=",
                "checkValue": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.commonEdit;
                    params.title = params.title || "编辑";
                    var url = "/" + params.url + "?",
                        key;
                    for(key in params.form_data){
                        url += key + "=" + params.form_data[key] + "&"
                    }
                    // url = url.substring(0, url.length-1);
                    if(scope.checkValue.length > 1 && params.single == 1){
                        parent.layer.msg(params.title + "的选项只能为一项", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        })
                    } else if (scope.checkValue.length == 0){
                        parent.layer.msg("请先选择要" + params.title + "的选项！", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        })
                    } else {
                        var checkValue = scope.checkValue.join(",");
                        parent.layer.open({
                            type: 2,
                            title: params.title,
                            shadeClose: false,
                            maxmin: true,
                            shade: 0.3,
                            area: ["750px", "650px"],
                            content: url + "id=" + checkValue  //编辑的页面地址，需要传递id给后台
                        });
                    }
                })
            }
        }
    })
    //只发送请求
    .directive("dyPost", function($http, postUrl){
        return {
            require: "?ngModel",
            scope: {
                "dyPost": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.dyPost;
                    postUrl.events("/" + params.url).success(function(_data){
                        if(_data.status == 200){
                            parent.layer.msg(_data.description, {icon: 1, time: 1000}, function(){
                                if(!!params.refresh_sub){
                                    window.parent.document.getElementById("rightDialog").contentWindow.location.reload(true);
                                } else {
                                    // window.parent.document.getElementById("rightcontent").contentWindow.location.reload(true);
                                    top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                }
                                layer.closeAll();
                            });
                        }else{
                            parent.layer.msg(_data.description, {icon: 2, time: 1000});
                        }
                    });
                })
            }
        }
    })
    //删除
    .directive("dyDel", function($http,postUrl){
        return {
            require: "?ngModel",
            scope: {
                "dyDel": "=",
                "checkValue": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.dyDel;
                    params.title = params.title || "删除";
                    var btnStatus = true;
                    var _recallAjax = function(){
                        parent.layer.confirm("确定是否" + params.title, {
                            time: 0, //不自动关闭
                            icon: 3,
                            shade: 0.3,
                            title: params.title,
                            btn: ["确定", "取消"]
                        }, function(index){
                            if(btnStatus){
                                btnStatus = false;
                                var checkValue = scope.checkValue.join(",");
                                postUrl.events("/" + params.url, {id: checkValue}).success(function(_data){
                                    if(_data.status == 500){  //返回错误提示
                                        parent.layer.msg(_data.description, {
                                            icon: 2,
                                            shade: 0.3,
                                            time: 3000
                                        })
                                    } else if (_data.status == 600){  //返回超时弹窗
                                        parent.layer.open({
                                            type: 2,
                                            title: "超时说明",
                                            shadeClose: false,
                                            maxmin: true,
                                            shade: 0.3,
                                            area: ["520px", "460px"],
                                            content: "/workbench/buss/addOutExp?id=" + _data.description
                                        });
                                    } else {
                                        parent.layer.msg(_data.description, {icon: 1, time: 1000}, function(){
                                            if(!!params.refresh_tree){  //组织架构-部门管理全部刷新，否则左侧的树数据无法更新
                                                window.parent.document.getElementById("rightcontent").contentWindow.location.reload(true);
                                            } else {
                                                top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                            }
                                            layer.closeAll();
                                        });
                                    }
                                });
                            } else {
                                return;
                            }
                        });
                    }
                    if(scope.checkValue.length > 1 && params.single == 1){
                        parent.layer.msg(params.title + "的选项只能为一项", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        })
                    } else if (scope.checkValue.length == 0){
                        parent.layer.msg("请先选择要" + params.title + "的选项！", {
                            icon: 2,
                            shade: 0.3,
                            time: 2000
                        });
                    } else {
                        if(!!params.double){  //二手房-待保证号的完成报证-二次确认
                            parent.layer.confirm("请确认是否已经导出", {
                                time: 0, //不自动关闭
                                icon: 3,
                                shade: 0.3,
                                title: "导出确认",
                                btn: ["确定", "取消"]
                            }, function(){
                                _recallAjax();
                            });
                        }else{
                            _recallAjax();
                        }
                    }
                })
            }
        }
    })
    //表单提交
    //<input type="submit" class="submit-btn" dy-submit="{url: formStruct.submit_url}" form-data="formData" value="提交">
    .directive("dySubmit", function(postUrl, scopeService){
        return {
            scope: {
                dySubmit: "=",
                dyFormData: "=formData",
                btnStatus: "=btnStatus"  //给按钮添加disabled的状态
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    scope.$apply(function () {
                        scope.btnStatus = true;  //防止重复提交
                    })
                    postUrl.events("/" + scope.dySubmit.url, scope.dyFormData).success(function(_data){
                        if(_data.status == 200){
                            parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1000}, function(){
                                scopeService.safeApply(scope, function () {
                                    scope.btnStatus = false;
                                });
                                if(!!scope.dySubmit.refresh_sub){
                                    window.parent.document.getElementById("rightDialog").contentWindow.location.reload(true);
                                } else if(!!scope.dySubmit.refresh_all){
                                    top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                    window.parent.document.getElementById("rightDialog").contentWindow.location.reload(true);
                                } else {
                                    // window.parent.document.getElementById("rightcontent").contentWindow.location.reload(true);
                                    top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                    window.parent.document.getElementById("frameSlideRight").style.right = "-920px";
                                }
                                parent.layer.closeAll();
                            });
                        }else{
                            parent.layer.msg(_data.description, {icon: 2, shade: 0.3, time: 1000}, function(){
                                scopeService.safeApply(scope, function () {
                                    scope.btnStatus = false;
                                });
                            });
                        }
                    });
                })
            }
        }
    })
    //表单提交前的确认提示
    //<input type="submit" class="submit-btn" dy-submit-confirm="{url: formStruct.submit_url, text: formStruct.text}" form-data="formData" value="提交">
    .directive("dySubmitConfirm", function(postUrl){
        return {
            scope: {
                dySubmitConfirm: "=",
                dyFormData: "=formData"
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var btnStatus = true;
                    parent.layer.confirm(scope.dySubmitConfirm.text || "是否已经打印完成？", {
                        time: 0, //不自动关闭
                        icon: 3,
                        shade: 0.3,
                        title: "确认",
                        btn: ["确定", "取消"]
                    }, function(){
                        if(btnStatus){
                            btnStatus = false;
                            postUrl.events("/" + scope.dySubmitConfirm.url, scope.dyFormData).success(function(_data){
                                if(_data.status == 200){
                                    parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1000}, function(){
                                        // window.parent.document.getElementById("rightcontent").contentWindow.location.reload(true);
                                        top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                        window.parent.document.getElementById("frameSlideRight").style.right = "-920px";
                                        parent.layer.closeAll();
                                    });
                                }else{
                                    parent.layer.msg(_data.description, {icon: 2, shade: 0.3, time: 1000}, function(){
                                        //window.location.reload();
                                    });
                                }
                            });
                        } else {
                            return;
                        }
                    });
                })
            }
        }
    })
    //<input type="submit" class="submit-btn" layer-confirm="{url: formStruct.submit_url, text: formStruct.text, title: formStruct.title}" form-data="formData" value="提交">
    .directive("layerConfirm", function(postUrl){
        return {
            scope: {
                layerConfirm: "=",
                dyFormData: "=formData"
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var btnStatus = true;
                    parent.layer.confirm(scope.layerConfirm.text || "是否确认？", {
                        time: 0, //不自动关闭
                        icon: 3,
                        shade: 0.3,
                        title: scope.layerConfirm.title || "确认",
                        btn: ["确定", "取消"]
                    }, function(){
                        if(btnStatus){
                            btnStatus = false;
                            postUrl.events("/" + scope.layerConfirm.url, scope.dyFormData).success(function(_data){
                                if(_data.status == 500){
                                    parent.layer.msg(_data.description, {
                                        icon: 2,
                                        shade: 0.3,
                                        time: 3000
                                    })
                                } else if(_data.status == 600){  //返回超时弹窗
                                    parent.layer.open({
                                        type: 2,
                                        title: "超时说明",
                                        shadeClose: false,
                                        maxmin: true,
                                        shade: 0.3,
                                        area: ["520px", "460px"],
                                        content: "/workbench/buss/addOutExp?id=" + _data.description
                                    });
                                } else {
                                    parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1000}, function(){
                                        if(!!scope.layerConfirm.refresh_sub){
                                            window.parent.document.getElementById("rightDialog").contentWindow.location.reload(true);
                                        } else if(!!scope.layerConfirm.refresh_all){
                                            top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                            window.parent.document.getElementById("rightDialog").contentWindow.location.reload(true);
                                        } else if(!!scope.layerConfirm.reload){
                                            window.location.reload();
                                        } else {
                                            // window.parent.document.getElementById("rightcontent").contentWindow.location.reload(true);
                                            top.frames["rightcontent"].reloadings();  //调用table页面的自定义函数刷新当前页面
                                            window.parent.document.getElementById("frameSlideRight").style.right = "-920px";
                                        }
                                        parent.layer.closeAll();
                                    });
                                }
                            });
                        } else {
                            return;
                        }
                    });
                })
            }
        }
    })
    //弹窗取消按钮
    .directive("layerClose", function(){
        return {
            link: function(scope, element, attrs){
                element.click(function(){
                    parent.layer.closeAll();
                })
            }
        }
    })

    //打开弹窗
    .directive("layerOpen", function(postUrl){
        return {
            restrict: "A",
            scope: {
                "layerOpen": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.layerOpen,
                        param = params.param ? "&param=" + params.param : "";
                    if(!!params.reback){  //侧滑头部打印模版弹窗
                        if(!!params.id){
                            parent.layer.open({
                                type: 2,
                                title: params.title || "审核",
                                shadeClose: false,
                                maxmin: true,
                                shade: 0.3,
                                area: ["750px", "650px"],
                                content: "/" + params.url + "?id=" + params.id + param
                            });
                        } else {
                            parent.layer.msg("请选择打印模板！", {icon: 2, time: 2000});
                        }
                    } else {
                        var url = params.url;
                        if(url.indexOf('&') == 0){
                            url = params.col[url.substr(1)];
                        }
                        var srcUrl = url.indexOf('http') == 0?url:("/" + url + "?id=" + params.id + param);
                        if(params.type == "full"){  //是否全屏显示
                            var index = parent.layer.open({
                                type: 2,
                                title: params.title || "审核",
                                shadeClose: false,
                                maxmin: false,
                                shade: 0.3,
                                area: ["750px", "650px"],
                                content: srcUrl
                            });
                            parent.layer.full(index);
                        } else {
                            parent.layer.open({
                                type: 2,
                                title: params.title || "审核",
                                shadeClose: false,
                                maxmin: true,
                                shade: 0.3,
                                area: [params.width || "750px", params.height || "650px"],
                                content: srcUrl
                            });
                        }
                    }
                })
            }
        }
    })

    //打开新窗口
    .directive("windowOpen", function(){
        return {
            restrict: "A",
            scope: {
                "windowOpen": "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                    var params = scope.windowOpen;
                    window.open("/" + params.url + "?id=" + params.id, "_blank", "width=800, height=600, top=0px, left=0px");
                })
            }
        }
    })

    //table
    .directive("tableList", function($rootScope,postUrl){
        return {
            restrict: "A",
            require: "?ngModel",
            templateUrl:'/ngtpl/tableList.html',
            scope: {
                tableList: "="
            },
            link: function(scope, element, attrs){
                scope.listUrl = scope.tableList.listUrl;
                scope.tableHeader = scope.tableList.tableHeader;
                scope.search = scope.tableList.search;
                scope.toollist = scope.tableList.toollist;
                if(scope.tableList.ext){
                    scope.showPager = scope.tableList.ext.pager; //是否显示分页
                    scope.pageOptions = scope.tableList.ext.page_options; //分页条数的配置项
                    scope.srcData = scope.tableList.ext.srcData || {}
                }else{
                    scope.srcData = {}
                }

                scope.tableB = {};

                /****************
                    *获取列表数据
                    *params：1、url =>请求地址；
                             2、params：参数，
                                （1）、srcData =>搜索框数据；
                                （2）、filterListData =>筛选内容数据；
                                （3）、page =>页码参数
                *****************/
                scope.getData = function(url, params){
                    postUrl.events("/" + url, params).success(function (_data) {
                        scope.tableData = _data.data;
                        scope.tableB = _data.data.items;
                        scope.Total = _data.data.total_items; //总条数
                        scope.Pages = _data.data.total_pages; //总页数
                        scope.Epage = _data.data.epage; //每页条数
                        scope.reloadPage = _data.data.page; //当前页
                        scope.pageLis = [];
                        for(var i=scope.reloadPage-2;i<=scope.reloadPage+2;i++){
                            if(i>scope.Pages)break;
                            if(i<1) continue;
                            scope.pageLis.push(i);
                        }
                        layer.closeAll("loading");
                    })
                }

                scope.select = function(index){
                    scope.curSel = index;
                }

                //第一次请求，请求页面和页面结构数据，构建列表结构和自动请求列表的数据
                if(scope.listUrl){
                    scope.getData(scope.listUrl, scope.srcData); //自动请求第一次列表的数据
                }

                /*** 分页操作 ***/
                scope.gotoPage = function(currentPage, itemsPerPage){
                    if(!itemsPerPage){
                        itemsPerPage = scope.Epage;
                    }
                    //构建搜索数据，包含搜索栏，分页页码
                    var params = angular.extend({}, scope.srcData, {"page": currentPage, "limit": itemsPerPage});
                    var url = scope.listUrl;
                    scope.getData(url, params);
                }
                scope.nextPage = function(){
                    scope.gotoPage(scope.reloadPage+1);
                }
                scope.prevPage = function(){
                    scope.gotoPage(scope.reloadPage-1);
                }
                //一页显示条数操作
                scope.perChange = function(itemsPerPage){
                    //构建搜索数据，包含搜索栏，分页页码
                    var params = angular.extend({}, scope.srcData, {"page": 1, "limit": itemsPerPage});
                    var url = scope.listUrl;
                    scope.getData(url, params);
                }

                /*** 处理搜索栏搜索数据 ***/
                scope.srcSubmit = function(){
                    //构建搜索数据，包含搜索栏，分页页码
                    var params = angular.extend({}, scope.srcData, {"page": scope.currentPage || 1, "limit": scope.itemsPerPage || 10});
                    var url = scope.listUrl;
                    scope.getData(url, params);
                }
                scope.srcKeyup = function(e){
                    var keycode = window.event ? e.keyCode : e.which;
                    if(keycode == 13){
                        scope.srcSubmit();
                    }
                }
            }
        }
    })

