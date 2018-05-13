/*!
 * Created By:xihun;
 * Created Time:2016-11-07;
 * 网站公用指令
 */
var dyDir = angular.module("dyDir", ["dyService"])
	//转换时间戳指令，将php返回过来的10位的时间戳转换为13位
    .filter("timestamp", function() {
        var filterfun = function(time) {
            if(time){
                if(angular.isNumber(time) || time.length == 10){
                    return time += '000';
                }
                return time;
            }
            return "";
        };

        return filterfun;
    })
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
            templateUrl: "/ngtpl/upload.html",
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
                        area: ["770px", "550px"],
                        end : function(){
                        		params.cb && params.cb();
                        	},
                        content: "/" + params.url //iframe的url
                    });
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
                                layer.closeAll();
                                params.cb && params.cb();
                            });
                        }else{
                            parent.layer.msg(_data.description, {icon: 2, time: 1000});
                        }
                    });
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
    //<input type="submit" class="submit-btn" layer-confirm="{url: formStruct.submit_url, text: formStruct.text, title: formStruct.title}" form-data="formData" value="提交">
    .directive("layerConfirm", function(postUrl){
        return {
            scope: {
                layerConfirm: "="
            },
            link: function(scope, element, attrs){
                element.click(function(){
                	var params = scope.layerConfirm;
                	if(params.needId && !params.id){
                   	 	parent.layer.msg("请选择一条记录", {icon: 1, shade: 0.3, time: 1000});
                   	 	return;
                    }
                    var btnStatus = true;
                    parent.layer.confirm("是否确认【" +scope.layerConfirm.title +"】？" , {
                        time: 0, //不自动关闭
                        icon: 3,
                        shade: 0.3,
                        title: scope.layerConfirm.title || "确认",
                        btn: ["确定", "取消"]
                    }, function(){
                        if(btnStatus){
                            btnStatus = false;
                            postUrl.events("/" + scope.layerConfirm.url, {id:params.id}).success(function(_data){
                                if(_data.status == 500){
                                    parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 3000});
                                    
                                } else {
                                    parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1500}, function(){
                                        parent.layer.closeAll();
                                        params.cb && params.cb();
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
                    var url = params.url;
                    if(url.indexOf('&') == 0){
                        url = params.col[url.substr(1)];
                    }
                    
                    if(params.needId && !params.id){
                    	 parent.layer.msg("请选择一条记录", {icon: 1, shade: 0.3, time: 1000});
                    	 return;
                    }
                    var srcUrl = url.indexOf('http') == 0?url:("/" + url + "?id=" + params.id + param);
                    if(params.type == "full"){  //是否全屏显示
                        var index = parent.layer.open({
                            type: 2,
                            title: params.title || "审核",
                            shadeClose: false,
                            maxmin: false,
                            shade: 0.3,
                            area: ["770px", "550px"],
                            end : function(){
                        		params.cb && params.cb();
                        	},
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
                            end : function(){
                        		params.cb && params.cb();
                        	},
                            area: [params.width || "770px", params.height || "550px"],
                            content: srcUrl
                        });
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
                scope.exportTable = scope.tableList.exportTable;
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
                        scope.curSel = undefined;
                        scope.pageLis = [];
                        var min = 2,max = 2;
                        if(scope.reloadPage < 3){
                        	min = scope.reloadPage-1;
                        	max = 4-min;
                        }
                        if(scope.reloadPage > scope.Pages-2){
                        	max = scope.Pages - scope.reloadPage;
                        	min = 4-max;
                        }
                        for(var i=scope.reloadPage-min;i<=scope.reloadPage+max;i++){
                            if(i>scope.Pages)break;
                            if(i<1) continue;
                            scope.pageLis.push(i);
                        }
                        scope.editFlag = [];
                        for(var i=0;i<=scope.tableB.length;i++){
                        	var edit_ = {};
                        	for(var key in scope.tableB[i]){
                        		edit_.key = 0;
                        	}
                        	scope.editFlag.push(edit_);
                        }
                        layer.closeAll("loading");
                    })
                }

                scope.select = function(index){
                    scope.curSel = index;
                }
                
                scope.toggleEdit = function(index,field){
//                	if(!scope.editFlag[index][field]){
//                		scope.editFlag[index][field] = 1;
//                	}else{
//                		scope.editFlag[index][field] = 0;
//                	}
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
                scope.refresh = function(){
                	scope.gotoPage(scope.reloadPage);
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

