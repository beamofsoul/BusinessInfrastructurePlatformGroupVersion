if (vueContentObject) getVueObject().$destroy();

[vuePageSize, loadPageableDataUrl, tableColumnsName, tableColumnsKey, tableButtonsOnEachRow] = [
	10, 
	'departmentsByPage',
	['','ID','部门编码','部门名称','部门描述','排序','所属机构','上级部门','可用状态','注册日期','最后修改日期','操作'],
	['selection','id','code','name','descirption','sort','organization','parent','available','createDate','modifyDate','operation'],
	['rowUpdateButton#修改','rowDeleteButton#删除']
];

parseValuesOnTableEachRow = obj => ({
	id: obj.id,
	code: obj.code,
	name: obj.name,
	descirption: obj.descirption,
	sort: obj.sort,
	organization: obj.organization ? obj.organization.name : '无',
	parent: obj.parent ? obj.parent.name : '无',
	available: obj.available ? '启用' : '弃用',
	createDate:formatDate(obj.createDate),
	modifyDate:formatDate(obj.modifyDate)
});

const [departmentsUrl, departmentsLabel, organizationsUrl, organizationsLabel] = [
	(currentRequestMappingRootPath + '/getAllAvailableDepartments'),'请选择上级部门',
	'organization/getAllAvailableOrganizations','请选择上级部门'
];

vueContentBeforeCreate = () => {
	[customVueContentData, parentDataSelect, organizationDataSelect, availableDataSelect] = [
		{treeData: [{id: null, expand: false, title: '<i class="ivu-icon ivu-icon-ios-circle-filled" style="font-size: 16px;">', children: vueContentObject.vueRecordTotal != 0 ? [{}] : null}]},
		getDataList(departmentsUrl,departmentsLabel),
		getDataList(organizationsUrl,organizationsLabel),
		[{value: 'true', label: '启用'},{value: 'false', label: '弃用'}]
	];
};

setFormDataObject({id: -1, code: '', name: '', descirption: '', sort: 1, organization_id: nullAsNumber, parent_id: nullAsNumber, available: 'true'});

[queryFormItemName, queryFormItemKey, queryFormItemType] = [
	['ID','部门编码','部门名称','所属机构','上级部门','可用状态'],
	['id','code','name','organization','parent','available'],
	['string','string','string','string','string','select#availableDataSelect']
];

//form 验证信息 
setFormRulesObject({
	'code': [{trigger: 'blur',type: 'string', required: true, min:4,max :10,message: '部门编码为长度4至10位之间字符串!'}, {validator: this.validateFormRules, trigger: 'blur',unique:'checkDepartmentCodeUnique',message: '部门编码已被使用'}],
	'name': [{trigger: 'blur',type: 'string', required: true, min:2,max :12,message: '部门名称为长度2至12位之间字符串!'}],
	'sort': [{trigger: 'blur',type: 'number', required: true, pattern: /^[0-9]*$/, message: '排序必须为正整数!'}]
});

//////////////////////////////// tree //////////////////////////////////

loadTreeNodeUrl = 'department/children';
vueContentMethods.selectChange = () => {};

parseNode = data => {
	let node = {id: data.id, expand: data.expand, title: data.name, children: data.countOfChildren != 0 ? [{}] : null};
	if (data.hasOwnProperty('available')) {
		node.disabled = !data.available;
		if (node.disabled)
			node.title = `${node.title} &nbsp;<i class="ivu-icon ivu-icon-eye-disabled" style="font-size: 16px;">`; 
	}
	return node;
}

vueContentObject = new Vue(initializeContentOptions());
(() => {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
	getVueRefObject('tree').$children[0].handleExpand();
})();

//////////////////////////////// tree //////////////////////////////////

submitAddAfter = submitUpdateAfter = submitDeleteAfter = (data) => {
	vueContentObject.parentDataSelect = getDataList(departmentsUrl,departmentsLabel);
	vueContentObject.organizationDataSelect = getDataList(organizationsUrl,organizationsLabel);
	handleNodeMovement(data);
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
			//移动的子节点在当前树中，扫描并获取当前节点，并移至新父节点下
			child = getChildFromNode(data.id, rootNode);
			//判断是否当前新父节点在当前树中
			if (isParentOnTree) {
				//新父节点在当前树中，在新父节点下插入
				setChildToNode(child,parentId,rootNode);
			} else {
				//新父节点不在当前树中，暂时不做任何操作
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
	} else if (currentAction == actions.del && data.count > 0) {
		for(let r of data.ids) getChildFromNode(data.ids[r], rootNode);
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

showUpdateFormBefore = (form) => {
	//动态设置上级部门下拉菜单中哪些项目是不可选的
	const id = vueUpdateForm.id;
	$.iposty('getChildrenIds', {id: id}, (data) => {
		let ids = data.ids;
		ids.push(id);
		let options = getVueRefObject('updateFormParentSelection').$children[0].$children[2].$children;
		options.forEach(option => option.disabled = ids.includes(option.value));
	});
}

function getDataList(url,label) {
	//Iview解决resetFields不能清空Select选中项问题之前
	//https://github.com/iview/iview/issues/970
	//暂且用Javascript中数字类型最小值在clearNullStructureObject4JSON方法中表示null值进行处理
	let content = [{value: nullAsNumber, label: label}];
	$.posty(url, null, ({parents}) => parents.map(node => content.push({value: node.id, label: node.name})));
	return content;
}

function parseChild(child, data, checkChildren) {
	child.id = data.id;
	child.expand = false;
	child.name = data.name;
	child.available = data.available;
	child.countOfChildren = checkChildren ? data.countOfChildren : 0;
}