 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
				$scope.findOptionById($scope.entity.typeId);
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象 
		$scope.entity.typeId =  $scope.entity.typeRawId.id;
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.parentId = $scope.parentId;	// 设置上级Id
			serviceObject=itemCatService.add( $scope.entity);//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.findByParentId($scope.parentId);	// 重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	 
	//批量删除 
	$scope.dele=function(){
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
				function(response){
					if(response.success){
						$scope.selectIds=[];
						alert(response.msg);
					}					
				}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	$scope.parentId = 0; 	//默认上一级Id
	// 通过上级Id获得下级列表
	$scope.findByParentId = function(parentId){
		$scope.parentId = parentId;	// 查询时记录上一级Id
		itemCatService.findByParentId(parentId).success(
			function(response){
				$scope.list = response;
			}
		);
	}
	
	$scope.grade = 1; // 默认为1
	$scope.setGrade = function(value){
		$scope.grade = value;
	}
	
	// 读取列表
	$scope.selectList = function(P_entity){
		if($scope.grade == 1){	// 如果为1级
			$scope.entity_1 = null;
			$scope.entity_2 = null;
		}
		if($scope.grade == 2){	// 如果为2级
			$scope.entity_1 = P_entity;
			$scope.entity_2 = null;
		}
		if($scope.grade == 3){	// 如果为3级
			$scope.entity_2 = P_entity;
		}
		$scope.findByParentId(P_entity.id);
	}
	
	// 获得分类模板
	$scope.templateList={data:[]};//规格列表
	$scope.findTemplateList = function(){
		typeTemplateService.selectOptionList().success(
			function(response){
				$scope.templateList = {data:response};
			}
		);
	}
	
	// 获得已选的模板用于回显
	$scope.findOptionById = function(id){
		typeTemplateService.findOptionById(id).success(
			function(response){
				$scope.entity.typeRawId = response;
			}
		);
	}
});	
