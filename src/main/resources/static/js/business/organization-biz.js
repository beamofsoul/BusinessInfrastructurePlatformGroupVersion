//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) vueContentObject.$destroy();

//分页取数据url
loadPageableDataUrl = 'organizationsByPage';
//table column 显示名
tableColumnsName = ['','ID','名称','描述','排序','上级机构ID','是否可用','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
tableColumnsKey = ['selection','id#sortable','name#sortable','descirption','sort','parentId','available','operation'];
//table 每行需要的按钮 
tableButtonsOnEachRow = ['rowUpdateButton#修改','rowDeleteButton#删除'];
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
//queryFormItemName = ['ID','昵称','用户名','密码','邮箱地址','电话号码','状态','注册日期','数字'];
//queryFormItemKey = ['id','nickname','username','password','email','phone','status','createDate','number'];
//queryFormItemType = ['string','string','string','string','string','string','select#statusDataSelect','date','10<number<20'];
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
		statusDataSelect : [{value: '1',label: '启用'},{value: '0',label: '禁用'}],
		imgName : '',
		imgvisible : false,
		uploadList : []
	}
};
////////////////////////////// 自定义 vue  methods ////////////////////////////////

////////////////////////////// 覆盖 流程方法 实现个性化/////////////////////////////////

//////////////////tree///////////////////
loadTreeRootUrl = 'permission/single';
loadTreeRootDataFunction = function() {return {id: 1}}
loadTreeNodeUrl = 'permission/children';

vueContentMethods.toggleExpand = toggleExpand;
vueContentMethods.selectChange = selectChange;
vueContentMethods.checkChange = checkChange;
vueContentMethods.getCheckedNodes = getCheckedNodes;
vueContentMethods.getSelectedNodes = getSelectedNodes;
vueContentMethods.getSelectedNodes = getSelectedNodes;

var vueContentObject = new Vue(initializeContentOptions());

$(function() {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
	toggleExpand(vueContentObject.treeData[0]);
	vueContentObject.$refs.tree.$children[0].handleExpand(toggleExpand);
});