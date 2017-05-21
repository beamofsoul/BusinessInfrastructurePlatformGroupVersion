//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();

vuePageSize = 10;
//分页取数据url
loadPageableDataUrl = 'departmentsByPage';
//table column 显示名
var tableColumnsName = ['','ID','部门编码','部门名称','部门描述','排序','上级部门','可用状态','注册日期','最后修改日期','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
var tableColumnsKey = ['selection','id','code','name','descirption','sort','parent','available','createDate','modifyDate','operation'];
//table 每行需要的按钮 
var tableButtonsOnEachRow = ['rowUpdateButton#修改','rowDeleteButton#删除'];
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
	return {id: obj.id,
		code: obj.code,
		name: obj.name,
		descirption: obj.descirption,
		sort: obj.sort,
		parent: obj.parent ? obj.parent.name : '无',
		available: obj.available ? '启用' : '弃用',
		createDate:formatDate(obj.createDate,true),
		modifyDate:formatDate(obj.modifyDate,true)};
}

vueContentBeforeCreate = function() {
	this.parentSelection = '';
	this.parentDataSelect = getDepartmentList4Parent();
	this.availableDataSelect = [{value: 'true', label: '启用'},{value: 'false', label: '弃用'}];
};

//设置add update vue form data obj
setFormDataObject({id: -1, code: '', name: '', descirption: '', sort: 1, parent_id: -999999999, available: 'true'});

//综合查询 form
var queryFormItemName = ['ID','部门编码','部门名称','上级部门','可用状态'];
var queryFormItemKey = ['id','code','name','parent','available'];
var queryFormItemType = ['string','string','string','string','select#availableDataSelect'];

//form 验证信息 
setFormRulesObject({
	'code': [{trigger: 'blur',type: 'string', required: true, min:4,max :10,message: '部门编码为长度4至10位之间字符串!'}, {validator: this.validateFormRules, trigger: 'blur',unique:'checkDepartmentCodeUnique',message: '部门编码已被使用'}],
	'name': [{trigger: 'blur',type: 'string', required: true, min:2,max :12,message: '部门名称为长度2至12位之间字符串!'}],
	'sort': [{trigger: 'blur',type: 'number', required: true, pattern: /^[0-9]*$/, message: '排序必须为正整数!'}]
});

var vueContentObject = new Vue(initializeContentOptions());

function getDepartmentList4Parent() {
	//Iview解决resetFields不能清空Select选中项问题之前
	//https://github.com/iview/iview/issues/970
	//暂且用Javascript中数字类型最小值在clearNullStructureObject4JSON方法中表示null值进行处理
	var content = [{value: -999999999, label: '请选择上级部门'}];
	$.iposty('getAllAvailableDepartments', null, function(data) {
		data = data.parents;
		for(var r in data) {
			var item = {};
			item.value = data[r].id;
			item.label = data[r].name;
			content.push(item);
		}
	});
	return content;
}

submitAddAfter = submitUpdateAfter = submitDeleteAfter = function() {
	vueContentObject.parentDataSelect = getDepartmentList4Parent();
}

showUpdateFormBefore = function(form) {
	var id = vueUpdateForm.id;
	$.iposty('getChildrenIds', {id: id}, function(data) {
		var ids = data.ids;
		ids.push(id);
		var options = getVueRefObject('updateFormParentSelection').$children[0].$children[2].$children;
		for(var r in options) {
			var option = options[r];
			if (arrayContains(ids, option.value)) option.disabled = true;
			else option.disabled = false;
		}
	});
}
