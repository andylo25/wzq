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


