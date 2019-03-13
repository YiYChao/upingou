// 定义模块
var app = angular.module("upingou",[]);

// $sce 服务携程过滤器
app.filter("trustHtml", ["$sce", function($sce){
	return function(data){
		return $sce.trustAsHtml(data);
	}
}]);
