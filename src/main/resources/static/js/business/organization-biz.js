//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) vueContentObject.$destroy();

actions = {
	  'delete': {'key': 'delete', 'url': 'delete'},
	  'add': {'key': 'add', 'url': 'singleAdd'},
	  'update': {'key': 'update', 'url': 'singleUpdate'},
	  'copy': {'key': 'copy', 'url': 'singleAdd'},
	  'changeSort': {'key': 'changeSort', 'url': 'changeSort'},
	  'deleteNode': {'key': 'deleteNode', 'url': 'deleteNode'}

	};

//分页取数据url
loadPageableDataUrl = 'organizationsByPage';
//table column 显示名
tableColumnsName = ['ID','名称','描述','排序','上级机构ID','是否可用','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
tableColumnsKey = ['id#sortable','name#sortable','descirption','sort','parent_id','available','operation'];
//table 每行需要的按钮 
tableButtonsOnEachRow = ['rowInfoButton#查看详情'];
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
	return {id :obj.id,
		name :obj.name,
		descirption :obj.descirption,
		sort :obj.sort,
		parent :obj.parent,
		available :obj.available};
}

//设置add update vue form data obj
setFormDataObject({id:null,name: '',descirption: '',sort: 1,parent_id: nullAsNumber,available: true});
////综合查询 form
//hasQueryFrom = false;
queryFormItemName = ['此节点ID下数据'];
queryFormItemKey = ['selectedNodeId'];
queryFormItemType = ['string'];


//form 验证信息 
setFormRulesObject({
	'name': [{trigger: 'blur',type: 'string', required: true, min:2,max :12, message: '名称为长度2至12位之间字符串!'},{validator: this.validateFormRules, trigger: 'blur',unique:'checkNameUnique',message: '名称已被占用'}]
});

////////////////////////////// 在vue生命周期 BeforeCreate 自定义 data ////////////////////////////////
vueContentBeforeCreate = function(){
	customVueContentData = {
//		treeData: generateRootNode(),
		treeData: [{id: null, expand: false, title: '<i class="ivu-icon ivu-icon-ios-circle-filled" style="font-size: 16px;">', children: vueContentObject.vueRecordTotal != 0 ? [{}] : null}],
//		'organization/getAllAvailableOrganizations'
//		(currentRequestMappingRootPath + '/getAllAvailableDepartments')
		parentDataSelect: getDataList('organization/getAllAvailableOrganizations','请选择上级部门'),
		statusDataSelect : [{value: '1',label: '启用'},{value: '0',label: '禁用'}]
	}
};
//////////////////tree///////////////////
//loadTreeRootUrl = 'organization/single';
//loadTreeRootDataFunction = function() {return {id: 1}}
loadTreeNodeUrl = 'organization/children';

//var checkedTreeNodesId;
var deleteNodesIdObject;
var updateNodeParent;//更新时判断 是否修改过上级机构


//新增
function doAddTreeButton() {
    currentAction = actions.add;
    resetForm();
    getVueObject().vueAddModalVisible = true;
}
function submitAddTreeForm(){
	submitFormValidate(currentAction, function (data) {
		toastSuccess('提交成功!');
	    getVueObject().vueAddModalVisible = false;
	    resetForm();
	    vueContentObject.parentDataSelect = getDataList('organization/getAllAvailableOrganizations','请选择上级部门');
	    handleNodeMovement(data.created);
	});
}

//更新
function doUpdateTreeButton() {
	if (!selectedNodeObject ||selectedNodeObject.id == -1) {
		toastInfo('请点击组织机构名称!');
		return;
	}
	getSingleData(selectedNodeObject.id, updateBefore, function(data) {
		currentAction = actions.update;
		resetForm();
		copyProperties(data, getVueObject().vueUpdateForm);
		getVueObject().vueUpdateModalVisible = true;
		updateNodeParent = data.parent;//保存未修改前上级机构值
	});
}
function submitUpdateTreeForm(){
	
	//? ?????? 更新sort 问题未解决
	submitFormValidate(currentAction, function (data) {
		toastSuccess('更新成功!');
		getVueObject().vueUpdateModalVisible = false;
		resetForm();
		vueContentObject.parentDataSelect = getDataList('organization/getAllAvailableOrganizations','请选择上级部门');
		handleNodeMovement(data.updated);
	});
}

//删除
function doDeleteTreeButton(){
	if (!checkedNodesObject||checkedNodesObject.length==0) {
		toastInfo('请勾选要删除的机构!');
		return;
	}
	//判断勾选的是否有 父节点 ，如果有，则询问用户是否删除此父节点下 包含未选择的子节点 是否删除
	deleteNodesIdObject = hasNotCheckedChildInParent();
	if(deleteNodesIdObject.parentId.length>0){
		//存在勾选父节点
		getVueObject().vueDeleteMessage = "勾选的机构存在父机构，将删除此父机构下所有子机构。是否继续删除?";
	}else{
		getVueObject().vueDeleteMessage = "即将删除以勾选的机构。是否继续删除?";
	}
//	let checkedNodestitle = getTreeCheckedNodesTitle();
	currentAction = actions.deleteNode;
//	getVueObject().vueDeleteMessage = "即将删除 [" + checkedNodestitle.toString() + "] 是否继续删除?";
//	checkedTreeNodesId = getTreeCheckedNodesId(); //将要删除的id 赋值给data
	getVueObject().vueDeleteModalVisible = true;
	
}
function submitDeleteTreeForm(){
	getVueObject().vueDeleteProgressVisible = true;
	submitForm(currentAction, deleteNodesIdObject, function (data) {
		if (data.count > 0) {
			toastSuccess('删除成功');
			getVueObject().vueDeleteProgressVisible = false;
		} else {
			toastWarning('记录正被使用，禁止删除');
			getVueObject().vueDeleteProgressVisible = false;
		}
		getVueObject().vueDeleteModalVisible = false;
		vueContentObject.parentDataSelect = getDataList('organization/getAllAvailableOrganizations','请选择上级部门');
		handleNodeMovement({ids: getTreeCheckedNodesId(), count: data.count});
	}, function (errorMessage) {
		toastError(errorMessage);
		getVueObject().vueDeleteProgressVisible = false;
	});
}
   
//上移
function doUpRemoveButton(){
	submitUpAndDownRemove(true);
}
//下移
function doDownRemoveButton(){
	submitUpAndDownRemove(false);
}

//上移下移
function submitUpAndDownRemove(isUp){
	
	if(!selectedNodeObject||selectedNodeObject.id==-1){
		toastInfo('请选择机构!');
		return;
	}
	
	let beforeId = selectedNodeObject.id;
	getSingleData(beforeId, updateBefore, function(data) {
		currentAction = actions.changeSort;
		if(data.sort>0){
			let parentId=null;
			if(data.parent) parentId = data.parent.id;
			let afterId ;
			//根据parentId 取到 节点，取此节点的children数组。遍历数组 调换两个对象位置
			let rootNode = getVueObject().treeData;
			// 如果parentId为null 则为虚拟根下的一级节点
			let childrenArray = parentId == null ? rootNode[0].children:getChildFromNodeNotDelete(parentId, rootNode[0]).children;
			let selectedNodeObjectIndex = -1;
			
			for(let index in childrenArray){
				if(childrenArray[index].id == selectedNodeObject.id) {
					selectedNodeObjectIndex =Number(index);
					break;
				}
			}
			
			if(isUp){
				if(selectedNodeObjectIndex>0){
					afterId = childrenArray[selectedNodeObjectIndex - 1].id;
				}else{
					toastInfo('无法继续移动!');
				}
			}else{
				if(selectedNodeObjectIndex < childrenArray.length-1){
					afterId = childrenArray[selectedNodeObjectIndex + 1].id;
				}else{
					toastInfo('无法继续移动!');
				}
			}
			
			if(afterId)
				submitForm(currentAction, {'beforeId':beforeId,'afterId':afterId}, function (data) {
					toastSuccess('上移成功!');
					if(isUp) swapItems(childrenArray, selectedNodeObjectIndex, selectedNodeObjectIndex - 1);
					else swapItems(childrenArray, selectedNodeObjectIndex, selectedNodeObjectIndex + 1);
				});
		}else{
			toastInfo('无法继续移动!');
		}
	});	

}

function handleNodeMovement(data) {
	
	const treeDataStr = JSON.stringify(vueContentObject.treeData); //字符串类型树数据对象
	const rootNode = vueContentObject.treeData[0]; //树数据对象根节点对象
	const parent = data.parent; //存储移动后新父节点内容的对象
	const parentId = parent == null ? null : parent.id; //存储移动后新父节点ID的对象
	const isParentOnTree = treeDataStr.includes(`{"id":${parentId},`); //树数据对象中新父节点的位置
	const isChildOnTree = treeDataStr.includes(`{"id":${data.id},`); //树数据对象中移动后节点的位置
	let child = {}; //存储移动后节点内容的对象
	
	if (currentAction == actions.update) {
		if (isChildOnTree) {
			// 判断是否修改了 上级机构 ，如果未修改 位置不动 更新信息。 如果修改了 上级机构 删除节点 重新添加节点。
			if(updateNodeParent == data.parent){
				let childrenArray = getChildFromNodeNotDelete(data.id, rootNode);
				childrenArray.title = data.name;
			}else{
				//移动的子节点在当前树中，扫描并获取当前节点，并移至新父节点下
				let unUsedChild = getChildFromNode(data.id, rootNode);//为了删除节点
				//判断是否当前新父节点在当前树中
				if (isParentOnTree) {
					parseChild(child, data);
					child = parseNode(child);
					//新父节点在当前树中，在新父节点下插入
					setChildToNode(child,parentId,rootNode);
				} else {
					//新父节点不在当前树中，暂时不做任何操作
				}
			}
			
		} else {
			//移动的子节点不在当前树中，判断是否新父节点在当前树中
			if (isParentOnTree) {
				parseChild(child, data, true);
				child = parseNode(child);
				//寻找新父节点并为其插入移动过来的子节点
				setChildToNode(child,parentId,rootNode);
			} else {
				//新父节点不在当前树中，暂时不做任何操作
			}
		}
	} else if (currentAction == actions.deleteNode && data.count > 0) {
		for(let r of data.ids) {
			if(r) getChildFromNode(r, rootNode);
		}
	} else if (currentAction == actions.add) {
		if (isParentOnTree) {
			parseChild(child, data);
			child = parseNode(child);
			//寻找新父节点并为其插入移动过来的子节点
			setChildToNode(child,parentId,rootNode);
		} else {
			//新父节点不在当前树中，暂时不做任何操作
		}
	}
}

function parseChild(child, data, checkChildren) {
	child.id = data.id;
	child.expand = false;
	child.name = data.name;
	child.available = data.available;
	child.countOfChildren = checkChildren ? data.countOfChildren : 0;
}

//交换数组元素
var swapItems = function(arr, index1, index2) {
  arr[index1] = arr.splice(index2, 1, arr[index1])[0];
  return arr;
};

vueContentMethods.toggleExpand = toggleExpand;
vueContentMethods.checkChange = checkChange;
vueContentMethods.getCheckedNodes = getCheckedNodes;
vueContentMethods.getSelectedNodes = getSelectedNodes; 

//点击节点名称
vueContentMethods.selectChange = function(node){
	if(node.length!=0){
		selectedNodeObject = node[0];
		getVueObject()[currentQueryFormName].selectedNodeId = selectedNodeObject.id;//设置query from 
		getVueObject().doLoadPage();
	}else{
		selectedNodeObject = null;
	}
};

//获取下拉列表数据
function getDataList(url,label) {
	//Iview解决resetFields不能清空Select选中项问题之前
	//https://github.com/iview/iview/issues/970
	//暂且用Javascript中数字类型最小值在clearNullStructureObject4JSON方法中表示null值进行处理
	let content = [{value: nullAsNumber, label: label}];
	$.posty(url, null, ({parents}) => parents.map(node => content.push({value: node.id, label: node.name})));
	return content;
}

var vueContentObject = new Vue(initializeContentOptions());

$(function() {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
//	toggleExpand(vueContentObject.treeData[0]);
//	vueContentObject.$refs.tree.$children[0].handleExpand(toggleExpand);
	getVueRefObject('tree').$children[0].handleExpand();
	disableUpdateData(getVueRefObject('tree'));
	//为了解决 先选checkbox后点击select 出现的 上级checkbox被选中的情况，暂时不明白原因
	getVueObject().treeData[0].selected=false;
});