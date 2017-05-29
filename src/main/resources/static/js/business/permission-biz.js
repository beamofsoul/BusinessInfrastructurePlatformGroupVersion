if (vueContentObject) getVueObject().$destroy();

const [resourceTypes, availables] = [new Map([['menu','菜单'],['button','按钮']]),['弃用','启用']];

[vuePageSize, loadPageableDataUrl, tableColumnsName, tableColumnsKey, tableButtonsOnEachRow] = [
	10, 
	'permissionsByPage',
	['','ID','名称','映射链接','映射行为','分组','上级权限','资源状态','排序','可用状态','注册日期','最后修改日期','操作'],
	['selection','id','name','url','action','group','parentId','resourceType','sort','available','createDate','modifyDate','operation'],
	['rowUpdateButton#修改','rowDeleteButton#删除']
];

parseValuesOnTableEachRow = obj => ({
	id : obj.id,
	name : obj.name,
	url : obj.url,
	action : obj.action,
	group : obj.group,
	parentId : obj.parentId == null ? '无' : obj.parentId,
	resourceType : resourceTypes.get(obj.resourceType),
	sort : obj.sort,
	available : availables[Number(obj.available)],
	createDate : formatDate(obj.createDate,true),
	modifyDate : formatDate(obj.modifyDate,true)
});

vueContentBeforeCreate = () => {
	let typeContent = [];
	for (let key of resourceTypes.keys()) typeContent.push({value: key, label: resourceTypes.get(key)});
	typeDataSelect = typeContent;
	availableDataSelect = [{value: 'true', label: availables[1]},{value: 'false', label: availables[0]}];
};

setFormDataObject({id:-1,name: '',url: '',action: '',group: '',parentId: '',resourceType: resourceTypes.keys().next().value,sort: 1,available:'true'});

[queryFormItemName, queryFormItemKey, queryFormItemType] = [
	['ID','名称','映射链接','映射行为','分组','上级权限','资源类型','可用状态'],
	['id','name','url','action','group','parentId','resourceType','available'],
	['string','string','string','string','string','string','select#typeDataSelect','select#availableDataSelect']
];

setFormRulesObject({
	'name': [{trigger: 'blur',type: 'string', required: true, min:3,max :10,message: '名称为长度3至10位之间字符串!'}, {validator: this.validateFormRules, trigger: 'blur',unique:'checkPermissionNameUnique',message: '名称已被使用'}],
	'parentId': [{trigger: 'blur',type: 'string', required: true, pattern: /^[0-9]*$/, message: '上级节点必须为正整数!'}],
	'url': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-z]+\/{1}[a-z]+$/, message: '映射链接必须以一个左斜杠[/]分割的两个小写英文字符串组成!'}],
	'action': [{trigger: 'blur',type: 'string', required: true, pattern: /^[a-z]+\:{1}[a-z]+$/, message: '映射行为必须以一个冒号[:]分割的两个小写英文字符串组成!'}],
	'sort': [{trigger: 'blur',type: 'number', required: true, pattern: /^[0-9]*$/, message: '排序必须为正整数!'}],
	'group': [{trigger: 'blur',type: 'string', required: true, message: '分组为长度不限的模块中文名称字符串!'}],
});

var vueContentObject = new Vue(initializeContentOptions());