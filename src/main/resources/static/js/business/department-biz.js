//销毁上一个content页面遗留vueContentObject对象实例
if (vueContentObject) getVueObject().$destroy();

vuePageSize = 10;
//分页取数据url
loadPageableDataUrl = 'departmentsByPage';
//table column 显示名
var tableColumnsName = ['','ID','部门编码','部门名称','部门描述','排序','所属机构','上级部门','可用状态','注册日期','最后修改日期','操作'];
//table column 对应data中的属性名   全选 加 'selection' 项 , 操作 加 'operation' 项。
var tableColumnsKey = ['selection','id','code','name','descirption','sort','organization','parent','available','createDate','modifyDate','operation'];
//table 每行需要的按钮 
var tableButtonsOnEachRow = ['rowUpdateButton#修改','rowDeleteButton#删除'];
//格式化table行数据格式
parseValuesOnTableEachRow = function (obj) {
	return {id: obj.id,
		code: obj.code,
		name: obj.name,
		descirption: obj.descirption,
		sort: obj.sort,
		organization: obj.organization ? obj.organization.name : '无',
		parent: obj.parent ? obj.parent.name : '无',
		available: obj.available ? '启用' : '弃用',
		createDate:formatDate(obj.createDate),
		modifyDate:formatDate(obj.modifyDate)};
}

vueContentBeforeCreate = function() {
	customVueContentData = {
		treeData: [{id: null, expand: false, title: '<i class="ivu-icon ivu-icon-ios-circle-filled" style="font-size: 16px;">', children: vueContentObject.vueRecordTotal != 0 ? [{}] : null}]
	};
	this.parentDataSelect = getDepartmentList4Parent();
	this.organizationDataSelect = getOrganizationList();
	this.availableDataSelect = [{value: 'true', label: '启用'},{value: 'false', label: '弃用'}];
};

//设置add update vue form data obj
setFormDataObject({id: -1, code: '', name: '', descirption: '', sort: 1, organization_id: -999999999, parent_id: -999999999, available: 'true'});

//综合查询 form
var queryFormItemName = ['ID','部门编码','部门名称','所属机构','上级部门','可用状态'];
var queryFormItemKey = ['id','code','name','organization','parent','available'];
var queryFormItemType = ['string','string','string','string','string','select#availableDataSelect'];

//form 验证信息 
setFormRulesObject({
	'code': [{trigger: 'blur',type: 'string', required: true, min:4,max :10,message: '部门编码为长度4至10位之间字符串!'}, {validator: this.validateFormRules, trigger: 'blur',unique:'checkDepartmentCodeUnique',message: '部门编码已被使用'}],
	'name': [{trigger: 'blur',type: 'string', required: true, min:2,max :12,message: '部门名称为长度2至12位之间字符串!'}],
	'sort': [{trigger: 'blur',type: 'number', required: true, pattern: /^[0-9]*$/, message: '排序必须为正整数!'}]
});

//////////////////////////////// tree //////////////////////////////////

loadTreeNodeUrl = 'department/children';
vueContentMethods.selectChange = function(){};

function parseNode(data) {
	var node = {id: data.id, expand: data.expand, title: data.name, children: data.countOfChildren != 0 ? [{}] : null};
	if (data.hasOwnProperty('available')) {
		node.disabled = !data.available;
		if (node.disabled)
			node.title = node.title + '&nbsp;<i class="ivu-icon ivu-icon-eye-disabled" style="font-size: 16px;">';
	}
	return node;
}

var vueContentObject = new Vue(initializeContentOptions());

$(function() {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
	vueContentObject.$refs.tree.$children[0].handleExpand();
});

////////////////////////////////tree //////////////////////////////////

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

function getOrganizationList() {
	//Iview解决resetFields不能清空Select选中项问题之前
	//https://github.com/iview/iview/issues/970
	//暂且用Javascript中数字类型最小值在clearNullStructureObject4JSON方法中表示null值进行处理
	var content = [{value: -999999999, label: '请选择所属机构'}];
	$.posty('organization/getAllAvailableOrganizations', null, function(data) {
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

submitAddAfter = submitUpdateAfter = submitDeleteAfter = function(data) {
	vueContentObject.parentDataSelect = getDepartmentList4Parent();
	vueContentObject.organizationDataSelect = getOrganizationList();
	handleNodeMovement(data);
}

function handleNodeMovement(data) {
	var treeDataStr = JSON.stringify(vueContentObject.treeData); //字符串类型树数据对象
	var rootNode = vueContentObject.treeData[0]; //树数据对象根节点对象
	var parent = data.parent; //存储移动后新父节点内容的对象
	var parentId = parent == null ? null : parent.id; //存储移动后新父节点ID的对象
	var parentIndex = treeDataStr.indexOf("{\"id\":"+parentId+","); //树数据对象中新父节点的位置
	var child = {}; //存储移动后节点内容的对象
	var childIndex = treeDataStr.indexOf("{\"id\":"+data.id+","); //树数据对象中移动后节点的位置
	
	if (currentAction == actions.update) {
		if (childIndex != -1) {
			//移动的子节点在当前树中，扫描并获取当前节点，并移至新父节点下
			child = getChildFromNode(data.id, rootNode);
			//判断是否当前新父节点在当前树中
			if (parentIndex != -1) {
				//新父节点在当前树中，在新父节点下插入
				setChildToNode(child,parentId,rootNode);
			} else {
				//新父节点不在当前树中，暂时不做任何操作
			}
		} else {
			//移动的子节点不在当前树中，判断是否新父节点在当前树中
			if (parentIndex != -1) {
				child.id = data.id;
				child.expand = false;
				child.name = data.name;
				child.available = data.available;
				child.countOfChildren = data.countOfChildren;
				child = parseNode(child);
				//寻找新父节点并为其插入移动过来的子节点
				setChildToNode(child,parentId,rootNode);
			} else {
				//新父节点不在当前树中，暂时不做任何操作
			}
		}
	}
}

/**
 * 根据输入的节点id在某一个节点下获取节点对象，并在原有父节点下删除其数据对象
 * @param id 要查找的节点id
 * @param node 包含该子节点的节点对象
 * @returns 找到的节点对象
 */
function getChildFromNode(id, node) {
	var target = null;
	var inCurrentNode = false;
	var children = node.children;
	for (var r in children) {
		var child = children[r];
		if (child.id == id) {
			target = child;
			inCurrentNode = true;
		} else {
			target = getChildFromNode(id, child);
		}
		if (target != null) {
			if (inCurrentNode) {
				node.children.splice(r,1);
				if (node.children.length == 0) node.children = null;
			}
			return target;
		}
	}
}

/**
 * 根据输入的父节点id在输入的节点对象中查找该父节点对象的位置，并将输入的子节点插入到该父节点对象下
 * @param child 将被插入的子节点对象
 * @param parentId 父节点id
 * @param node 包含该父节点的节点对象
 */
function setChildToNode(child, parentId, node) {
	if (node.id != parentId) {
		var children = node.children;
		if (children) {
			for(var r in children) {
				setChildToNode(child, parentId, children[r]);
			}
		}
	} else {
		if (node.children) {
			node.children.push(child);
		} else {
			node.children = [child];
		}
	}
}

showUpdateFormBefore = function(form) {
	//动态设置上级部门下拉菜单中哪些项目是不可选的
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