
var dyService = angular.module("dyService", [])
    .run(['$rootScope', 'scopeService', function ($rootScope, scopeService) {
    	/**
         * 释放禁用提交按钮
         * formName {String}    表单name
         * status {Boolean}     禁用状态
         */
        $rootScope.updateDisabledSub = function (formName, status) {
            if (formName) {
                scopeService.safeApply($rootScope, function () {
                    $rootScope.Is_submitted[formName] = status;
                });
            }
        }
        $rootScope.user_ = window.user_ || {};
        $rootScope.menus_ = window.menus_ || {};
        $rootScope.pageTitle_ = window.pageTitle_ || {};
        $rootScope.copyright = "&copy; Andy cui 2018";
    }])
    .factory('scopeService', function () {
        return {
            safeApply: function ($scope, fn) {
                var phase = $scope.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && typeof fn === 'function') {
                        fn();
                    }
                } else {
                    $scope.$apply(fn);
                }
            }
        };
    })
    /**
     * $http请求服务
     * postUrl.events(url, data).success(function(_data){
     *  //Act on the event
     * })
     */
    .factory('postUrl', ['$http', function ($http) {
        var doRequest = function (url, data) {
            return $http({
                method: 'post',
                url: url,
                data: data ? $.param(data) : '',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
        };
        return {
            events: function (url, data) {
                return doRequest(url, data);
            }
        }
    }])
    .factory('submitForm', ['$http', function ($http) {
        var submitForm = function (url, data) {
            if(url && data){
                var eps = $("#dy_common_submit_form__");
                if(eps.length <= 0){
                    eps = $("<form name='dy_common_submit_form__' id='dy_common_submit_form__' method='post' action='" + url + "'></form>")
                    $('body').append(eps);
                }else{
                    eps.empty();
                }
                for(var attr in data){
                    eps.append($("<input name='" + attr + "' type='hidden' value='" + data[attr] + "'/>"));
                }
                eps.submit();
            }
        };
        return {
            submit: function (url, data) {
                return submitForm(url, data);
            }
        }
    }])
    .factory('ajaxUrl', ['$rootScope', 'postUrl', function ($rootScope, postUrl) {
        return {
            events: function (url, callbackFn) {
                var def = {
                    reload: true
                }, callbackFn = angular.extend({}, def, callbackFn);
                $rootScope.updateDisabledSub(callbackFn.formName, true);
                callbackFn.data = callbackFn.data || '';
                postUrl.events(url, callbackFn.data).success(function (data) {
                    var dataType_json = (typeof(data) === "object");
                    if (dataType_json) {
                        if (data.status == "250") {
                            parent.layer.msg("您还未登录或登录超时，请先登录", 2, 0, function () {
                                window.top.location.href = '/system/public/login';
                            });
                        } else {
                            $rootScope.updateDisabledSub(callbackFn.formName, false);
                            return callbackFn.json(data);
                        }
                    } else {
                        return callbackFn.html(data);
                    }
                });
            }
        }
    }])
    .factory("getUrlParams", ["$rootScope", "$http", function($rootScope, $http){
    	var getParams = {};
        getParams.events = function(){
            $rootScope.params = {};
            var params_ = [];
            var url = location.search; //获取url中"?"符后的字串
            if(url && url.indexOf("?") != -1){ // search参数
            	params_ = url.substr(1).split("&");
            	$rootScope.params.pageUrl = location.pathname.slice(1);
            }else{ // &连接的参数
            	params_ = location.pathname.slice(1).split("&");
            	$rootScope.params.pageUrl = params_.shift();
            }
            for(var i in params_){
        		var temp = params_[i].split("=");
        		$rootScope.params[temp[0]] = temp[1];
            }
        }
        return getParams;
    }]);

