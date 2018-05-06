var admin = angular.module("admin", ["dyDir", "dyService"]);

admin.controller("loginCtrl", function($scope, $http, postUrl,scopeService){
//	$scope.formData = page_struct.data;
	var initbtnText = "登录";
	$scope.btnText = initbtnText;
	$scope.submitForm = function(){
		$scope.btnText = "登录中...";
		postUrl.events('/admin/login', $scope.formData).success(function(_data) {
			if(_data.status==200){
				parent.layer.msg(_data.description, {icon: 1, shade: 0.3, time: 1500},function(){
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

//分页
admin.controller('tableCtrl', function($scope, $http, postUrl) {

	$scope.currentStatus = 0;
	$scope.selectTab = function(url, index){
		if(index!=0){
			$scope.rowLink=null;
		}else{
			$scope.rowLink=table_struct.rowLink;
		}
		$scope.currentStatus = index;
		$scope.getData(url);
	}
	

    layer.load(1);
    $scope.srcData = {};
    $scope.filterListData = {};
    /****************
        *获取列表数据
        *params：1、url =>请求地址；
                 2、params：参数，
                    （1）、srcData =>搜索框数据；
                    （2）、filterListData =>筛选内容数据；
                    （3）、page =>页码参数
    *****************/
    $scope.getData = function(url, params){
        postUrl.events("/" + url, params).success(function (_data) {
            $scope.tableData = _data.data;
            $scope.tableB = _data.data.items;
            $scope.Total = _data.data.total_items; //总条数
            $scope.Pages = _data.data.total_pages; //总页数
            $scope.Epage = _data.data.epage; //每页条数
            $scope.reloadPage = _data.data.page; //当前页
            layer.closeAll("loading");
        })
    }

    //var url = "/tablelist.php"; //请求数据需要用到的接口，一般是默认用window.location.href
    //第一次请求，请求页面和页面结构数据，构建列表结构和自动请求列表的数据
    if(typeof table_struct != "undefined" && table_struct != ""){
    	if(table_struct.search && angular.isArray(table_struct.search)){
    		$scope.search = table_struct.search[0]; //搜索框
    	}else{
    		$scope.search = table_struct.search; //搜索框
    	}
        $scope.showPager = table_struct.pager; //是否显示分页
        $scope.pageOptions = table_struct.page_options; //分页条数的配置项
        $scope.tableHeader = table_struct.tableHeader; //列表表头
        $scope.rowLink = table_struct.rowLink;  //操作栏中的多个操作处理

        $scope.formData = {};
        $scope.formList = table_struct.header_data;
        var initformdata = $scope.formList;
        if(initformdata != ""){
            for (var key in initformdata) {
                $scope.formData[key] = initformdata[key];
            }
        }

        $scope.listUrl = table_struct.listUrl;
        if($scope.listUrl){
            $scope.getData($scope.listUrl, $scope.formData); //自动请求第一次列表的数据
        }
        layer.closeAll("loading");
    }

    /*** 分页操作 ***/
    $scope.gotoPage = function(currentPage, itemsPerPage){
        if(!itemsPerPage){
            itemsPerPage = $scope.Epage;
        }
        //构建搜索数据，包含搜索栏，分页页码
        var params = angular.extend({}, $scope.srcData, {"page": currentPage, "limit": itemsPerPage}, $scope.formData);
        var url = $scope.listUrl;
        $scope.getData(url, params);
    }
    //一页显示条数操作
    $scope.perChange = function(itemsPerPage){
        //构建搜索数据，包含搜索栏，分页页码
        var params = angular.extend({}, $scope.srcData, {"page": 1, "limit": itemsPerPage}, $scope.formData);
        var url = $scope.listUrl;
        $scope.getData(url, params);
    }

    /*** 处理搜索栏搜索数据 ***/
    $scope.srcSubmit = function(){
        //构建搜索数据，包含搜索栏，分页页码
        var params = angular.extend({}, $scope.srcData, {"page": $scope.currentPage || 1, "limit": $scope.itemsPerPage || 10}, $scope.formData);
        var url = $scope.listUrl;
        $scope.getData(url, params);
    }
    $scope.srcKeyup = function(e){
        var keycode = window.event ? e.keyCode : e.which;
        if(keycode == 13){
            $scope.srcSubmit();
        }
    }
});

