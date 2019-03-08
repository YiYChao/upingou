app.controller("baseController",function($scope){
	
	// 刷新列表
	$scope.reloadList = function(){
		// 切换页码
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	};
	
	//分页控件配置 
	$scope.paginationConf = {
			 currentPage: 1,
			 totalItems: 10,
			 itemsPerPage: 10,
			 perPageOptions: [10, 20, 30, 40, 50],
			 onChange: function(){	// 页面加载时就会触发此方法
				 $scope.reloadList();//重新加载
			 }
	};
	
	$scope.selectIds = [];	// 选中的Id集合
	$scope.updateSelection = function($event, id){
		if($event.target.checked){	// 如果被选中，则增加到数组
			$scope.selectIds.push(id);
		}else{
			var index = $scope.selectIds.indexOf(id);	// 获得Id在数组中的下标
			$scope.selectIds.splice(index,1);	// 从数组中移除元素
		}
	};
	
	//提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
	$scope.json2String = function(jsonString,key){
		var json = JSON.parse(jsonString);
		var value = "";
		for(var i = 0;i<json.length;i++){
			if(i > 0){
				value += ",\t";
			}
			value += json[i][key];
		}
		return value;
	}
	
});