var admin = angular.module("admin", ["dyDir", "dyService"]);

admin.controller("indexCtrl", function($scope, $http, postUrl,scopeService){
	$scope.formData = page_struct.formData;
    $scope.formStruct = page_struct.formStruct;

	$scope.submitForm = function(){
		postUrl.events('/admin/login', $scope.formData).success(function(_data) {
			if(_data.status==200){
				parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1500},function(){
					scopeService.safeApply($scope, function () {
                        $scope.btnText = initbtnText;
                        $scope.btnStatus = false;
                    });
                    parent.location = "/";
					parent.layer.closeAll();
				});
			}else{
				parent.layer.msg(_data.description, {icon: 2, shade: 0.3, time: 1500}, function(){
					scopeService.safeApply($scope, function () {
                        $scope.btnText = initbtnText;
                        $scope.btnStatus = false;
                    });
				});
			}
		});
	}
	
});

admin.controller("addCtrl", function($scope, $http, postUrl,scopeService){
	$scope.formData = page_struct.formData;
    $scope.formStruct = page_struct.formStruct;

    var initbtnText = $scope.formStruct.btnText ? $scope.formStruct.btnText : "提 交";
    $scope.btnText = initbtnText;
    $scope.btnStatus = false;
    
	$scope.submitForm = function(){
		var subFormData = {};
    	if($scope.formStruct.beforeSubmit){
    		var result=$scope.formStruct.beforeSubmit();
    		if(!result){
    			scopeService.safeApply($scope, function () {
                	$scope.btnText = initbtnText;
                    $scope.btnStatus = false;
                });
    			return;
    		}
    	}
        for(var att in $scope.formData){
        	if(angular.isArray($scope.formData[att])){
        		if(typeof $scope.formData[att][0] == "object"){
        			subFormData[att] = angular.toJson($scope.formData[att],true);
        		}else {
        			subFormData[att] = $scope.formData[att].join(",");
        		}
        	}else {
        		subFormData[att] = $scope.formData[att];
        	}
        }
		postUrl.events('/'+ $scope.formStruct.submitUrl, subFormData).success(function(_data) {
			if(_data.status==200){
				parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1500},function(){
					scopeService.safeApply($scope, function () {
                        $scope.btnText = initbtnText;
                        $scope.btnStatus = false;
                    });
					parent.layer.closeAll();
					parent.location.reload();
				});
			}else{
				parent.layer.msg(_data.description, {icon: 2, shade: 0.3, time: 1500}, function(){
					scopeService.safeApply($scope, function () {
                        $scope.btnText = initbtnText;
                        $scope.btnStatus = false;
                    });
				});
			}
		});
	}
	
});


