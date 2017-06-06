if (vueContentObject) getVueObject().$destroy();

const entityFeatures = new Map([
	['父子关系', {name: 'parent', type: '####ENTITY####', annotations: '@ManyToOne#,#@JoinColumn(name = "PARENT_ID", nullable = true)'}],
	['排序索引', {name: 'sort', type: 'Integer', annotations: '@Column'}],
	['可用状态', {name: 'available', type: 'Boolean', annotations: '@Column'}],
	['多种分类', {name: 'type', type: 'Integer', annotations: '@Column'}],
	['引入用户', {name: 'user', type: 'User', annotations: '@ManyToOne#,#@JoinColumn(name = "USER_ID")'}]
]);

customVueContentData = {
	entityContent : {entityName: '', tableName: '', checkedFeatures: []},
    tableColumns: [{title: '属性名称', key: 'name'}, {title: '属性类型', key: 'type'}, {title: '相应注解', key: 'annotations'}, {title: '操作', key: 'operations',render: (h, params) => {return h('div', [h('Button', {on: {click: () => vueContentMethods.remove(params)}}, '删除')])}}],
    tableData: [],
    result: ''
};

/**
 * 实体类名称输入框值首字母自动大写, 并过滤掉转义字符和特殊字符
 */
vueContentMethods.capital = () => {
	let nameValue = getVueObject().entityContent.entityName;
	if (nameValue) {
		nameValue = nameValue.replace(/[\'\"\\\/\b\f\n\r\t]/g, '');
		nameValue = nameValue.replace(/[\@\#\$\%\^\&\*\{\}\:\"\L\<\>\?]/g, '');
	}
	getVueObject().entityContent.entityName = (nameValue && nameValue != 'undefined') ?  nameValue.replace(/( |^)[a-z]/g, (L) => L.toUpperCase()) : '';
}

vueContentMethods.remove = (params) => {
	// 取消checkbox选中状态
	let features = getVueObject().entityContent.checkedFeatures;
	for(let r in features)
		if (params.row.name === entityFeatures.get(features[r]).name)
			getVueObject().entityContent.checkedFeatures.splice(r, 1);
	// 删除数据表格中指定数据
	getVueObject().tableData.splice(params.index, 1);
}

/**
 * 数据表格中的数据根据选中的特性进行动态管理
 */
vueContentWatch = {
	'entityContent.checkedFeatures': {
		handler: function(val, oldVal) {
			let content = [];
			val.forEach(e => content.push(entityFeatures.get(e)));
			getVueObject().tableData = content;
		}
	}
}

/**
 * 自动拼接数据库表名
 */
vueContentComputed = {
	computedTableName: function() {
		let nameValue = this.entityContent.entityName;
		this.entityContent.tableName = nameValue ? 'T' + nameValue.replace(/([A-Z])/g,"_$1").toUpperCase() : ''; 
		return this.entityContent.tableName;
	}
}

vueContentObject = new Vue(initializeContentOptions());

function submitEntityForm() {
	let vm = getVueObject();
	vm.entityContent.checkedFeatures = vm.tableData;
	$.iposty('buildEntity',vm.entityContent, (data) => vm.result = data.instance);
	resetEntityForm();
}

function resetEntityForm() {
	getVueRefObject('entityForm').resetFields();
}

function deleteRow(current) {
	console.log(current);
}