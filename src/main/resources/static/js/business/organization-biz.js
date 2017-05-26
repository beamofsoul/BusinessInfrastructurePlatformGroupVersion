//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) vueContentObject.$destroy();

//分页取数据url
loadPageableDataUrl = 'organizationsByPage';
//table column 显示名
tableColumnsName = ['','ID','名称','描述','排序','上级机构ID','是否可用','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
tableColumnsKey = ['selection','id#sortable','name#sortable','descirption','sort','parentId','available','operation'];
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
setFormDataObject({id:null,name: '',descirption: '',sort: 1,parentId: null,available: true});
////综合查询 form
//hasQueryFrom = false;
queryFormItemName = ['此节点ID下数据'];
queryFormItemKey = ['selectedNodeId'];
queryFormItemType = ['string'];
//
//
////form 验证信息 
//setFormRulesObject({
//	'username': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z\d]\w{4,11}[a-zA-Z\d]$/, message: '用户名称必须为长度6至12位之间以字母、特殊字符(·)或数字字符组成的字符串!'},{validator: this.validateFormRules, trigger: 'blur',unique:'checkUsernameUnique',message: '用户名已被占用'}],
//	'password': [{trigger: 'blur',type: 'string', required: true, min:6,max :16,message: '密码为长度6至12位之间字符串!'},{validator: this.validateFormRules, trigger: 'blur',otherValidate:'repassword',message: '用户名已被占用'}],
//    'repassword': [{trigger: 'blur',type: 'string', required: true,message:'请输入确认密码'},{trigger: 'blur',type: 'string', validator: this.validateFormRules,equal:'password',message: '两次输入密码不一致!'}],
//    'nickname': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-zA-Z0-9·\u4e00-\u9fa5]{2,12}$/, message: '昵称必须为长度2至12位之间以字母、特殊字符(·)、汉字或数组字符组成的字符串!'},{validator: this.validateFormRules, trigger: 'blur',unique:'checkNicknameUnique',message: '昵称已被占用'}]
//});

////////////////////////////// 在vue生命周期 BeforeCreate 自定义 data ////////////////////////////////
vueContentBeforeCreate = function(){
	customVueContentData = {
		treeData: generateRootNode(),
		statusDataSelect : [{value: '1',label: '启用'},{value: '0',label: '禁用'}]
	}
};
//////////////////tree///////////////////
loadTreeRootUrl = 'organization/single';
loadTreeRootDataFunction = function() {return {id: 1}}
loadTreeNodeUrl = 'organization/children';

var selectedTreeId=-1;
//更新
function doUpdateTreeButton() {
	if (selectedTreeId == -1) {
		toastInfo('请点击组织机构名称!');
		return;
	}
	getSingleData(selectedTreeId, updateBefore, function(data) {
		currentAction = actions.update;
		resetForm();
		copyProperties(data, getVueObject().vueUpdateForm);
		getVueObject().vueUpdateModalVisible = true;
	});
}
function submitUpdateTreeForm(){
	submitFormValidate(currentAction, function (data) {
		toastSuccess('更新成功!');
		getVueObject().vueUpdateModalVisible = false;
		resetForm();
	});
}
var checkedTreeNodesId;
//删除
function doDeleteTreeButton(){
	
	if (!checkedNodesObject||checkedNodesObject.length==0) {
		toastInfo('请勾选要删除的机构!');
		return;
	}
	let checkedNodestitle = getTreeCheckedNodesTitle();
	console.log(checkedNodestitle);
	currentAction = actions.delete;
	getVueObject().vueDeleteMessage = "即将删除 [" + checkedNodestitle.toString() + "] 是否继续删除?";
	checkedTreeNodesId = getTreeCheckedNodesId(); //将要删除的id 赋值给data
	getVueObject().vueDeleteModalVisible = true;
	
}

//点击节点名称
vueContentMethods.selectChange = function(node){
	if(node.length!=0){
		selectedTreeId  = node[0].id;
		getVueObject()[currentQueryFormName].selectedNodeId = selectedTreeId;//设置query from 
		getVueObject().doLoadPage();
	}else{
		selectedTreeId=-1;
	}
};

vueContentMethods.toggleExpand = toggleExpand;
vueContentMethods.checkChange = checkChange;
vueContentMethods.getCheckedNodes = getCheckedNodes;
vueContentMethods.getSelectedNodes = getSelectedNodes;

var vueContentObject = new Vue(initializeContentOptions());

$(function() {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
	toggleExpand(vueContentObject.treeData[0]);
	vueContentObject.$refs.tree.$children[0].handleExpand(toggleExpand);
	disableUpdateData(getVueRefObject('tree'));
});