app.controller("brandController",function($scope,$http,$controller,brandService){
	
	$controller("baseController",{$scope:$scope});
	
	$scope.findAll = function(){
		brandServicefindAll().success(
				function(response){
					$scope.brandList = response;
				}
		);
	}
	
	// 分页函数
	$scope.findPage = function(page,rows){
		brandService.findPage(page,rows).success(
			function(response){
				$scope.brandList=response.rows;	// 显示当前页数据
				$scope.paginationConf.totalItems=response.total;	//更新总记录数
			}		
		);
	}
	
	$scope.save = function(){
		var object = null;	// 对象名称
		if($scope.entity.id != null){
			object = brandService.update($scope.entity);
		}else{
			object = brandService.add($scope.entity);
		}
		object.success(
			function(response){
				if(response.success){
					alert(response.msg);
					$scope.reloadList();
				}else{
					alert(response.msg);
				}
			}	
		);
	}
	
	$scope.findById = function(id){
		brandService.findById(id).success(
			function(response) {
				$scope.entity = response;
			}	
		);
	}
	
	$scope.deleteBrands = function(){
		brandService.deleteBrands($scope.selectIds).success(
			function(response){
				if(response.success){
					alert(response.msg);
					$scope.reloadList();
				}else{
					alert(response.msg);
				}
			}	
		);
	}
	
	$scope.searchEntity = {};	// 定义搜索对象
	$scope.search = function(page,rows){
		brandService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.paginationConf.totalItems = response.total;//总记录数 
				$scope.brandList = response.rows;//给列表变量赋值
			}		
		);
	}
	
});