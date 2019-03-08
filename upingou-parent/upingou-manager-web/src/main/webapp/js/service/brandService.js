// 定义品牌服务
app.service("brandService",function($http){
	this.findAll = function(){
		return $http.get("../brand/list.do");
	}
	
	this.findPage = function(page,rows){
		return $http.get("../brand/pageList/"+page+"/"+rows+".do");
	}
	
	this.findById = function(id){
		return $http.get("../brand/findById/"+id+".do");
	}
	
	this.add = function(entity){
		return $http.post("../brand/add.do",entity);
	}
	
	this.update = function(entity){
		return $http.post("../brand/update.do",entity);
	}
	
	this.deleteBrands = function(selectedIds){
		return $http.post("../brand/delete.do", selectedIds);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../brand/search/"+page+"/"+rows+".do",searchEntity);
	}
	// 读取品牌列表
	this.selectOptionList = function(){
		return $http.get("../brand/selectOptionList.do");
	}
});

