generateBreadcrumbItem = function() {
	return '<span><span class="ivu-breadcrumb-item-link">权限管理</span></span>';
}

//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();

defaultVueBindPageSizeData = 10;
// 当前用户能够操作的所有行为
var actions = {'del': {'key':'del','url':'delete'},'add': {'key':'add','url':'singleAdd'},'update':{'key':'update','url':'singleUpdate'},'copy':{'key':'copy','url':'singleAdd'}};
//分页取数据url
loadPageableDataUrl = 'permissionsByPage';
//table column 显示名
var tableColumnsName = ['','ID','名称','映射链接','映射行为','分组','上级权限','资源状态','排序','可用状态','注册日期','最后修改日期','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
var tableColumnsKey = ['selection','id','name','url','action','group','parentId','resourceType','sort','available','createDate','modifyDate','operation'];
//table 每行需要的按钮 
var tableButtonsOnEachRow = ['vueBindButtonUpdateMethod#修改','vueBindButtonDeleteMethod#删除'];
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
	return {id :obj.id,
		name :obj.name,
		url :obj.url,
		action :obj.action,
		group :obj.group,
		parentId :obj.parentId,
		resourceType: obj.resourceType === 'menu' ? '菜单' : '按钮',
		sort: obj.sort,
		available: obj.available ? '启用' : '弃用',
		createDate:formatDate(obj.createDate,true),
		modifyDate:formatDate(obj.modifyDate,true)};
}

setVueContentBeforeCreateFunction(function() {
	this.typeDataSelect = [{value: 'menu', label: '菜单'},{value: 'button', label: '按钮'}];
	this.availableDataSelect = [{value: 'true', label: '启用'},{value: 'false', label: '弃用'}];
	this.defaultVueBindCollapseQueryFormData = '-1';
});

//设置add update vue form data obj
setVueBindFormModelData({id:-1,name: '',url: '',action: '',group: '',parentId: '',resourceType: 'menu',sort: '1',available:'true'});

//综合查询 form
var queryFormItemName = ['ID','名称','映射链接','映射行为','分组','上级权限','资源类型','可用状态'];
var queryFormItemKey = ['id','name','url','action','group','parentId','resourceType','available'];
var queryFormItemType = ['string','string','string','string','string','string','select#typeDataSelect','select#availableDataSelect'];
//form 验证信息 
setVueBindFormRulesData({
	'name': [{trigger: 'blur',type: 'string', required: true, min:3,max :10,message: '名称为长度3至10位之间字符串!'}, {validator: this.vueFormRulesCommonValidate, trigger: 'blur',unique:'checkPermissionNameUnique',message: '名称已被使用'}],
	'parentId': [{trigger: 'blur',type: 'string', required: true, pattern: /^[0-9]*$/, message: '上级节点必须为正整数!'}],
	'url': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-z]+\/{1}[a-z]+$/, message: '映射链接必须以一个左斜杠[/]分割的两个小写英文字符串组成!'}],
	'action': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-z]+\:{1}[a-z]+$/, message: '映射行为必须以一个冒号[:]分割的两个小写英文字符串组成!'}],
	'sort': [{trigger: 'blur',type: 'string', required: true, pattern: /^[0-9]*$/, message: '排序必须为正整数!'}],
	'group': [{trigger: 'blur',type: 'string', required: true, message: '分组为长度不限的模块中文名称字符串!'}],
});

var vueContentObject = new Vue(initializeContentOptions());
