if (vueContentObject) getVueObject().$destroy();

const [regulars, availables] = [['否','是'],['弃用','启用']];

[vuePageSize, loadPageableDataUrl, tableColumnsName, tableColumnsKey, tableButtonsOnEachRow] = [
	10, 
	'sensitiveWordsByPage',
	['','ID','敏感词','替换词','是否正则表达式','可用状态','注册日期','最后修改日期','操作'],
	['selection','id','word','replacement','regular','available','createDate','modifyDate','operation'],
	['rowUpdateButton#修改','rowDeleteButton#删除']
];

parseValuesOnTableEachRow = obj => ({
	id: obj.id,
	word: obj.word,
	replacement: obj.replacement,
	regular: regulars[Number(obj.regular)],
	available: availables[Number(obj.available)],
	createDate:formatDate(obj.createDate,true),
	modifyDate:formatDate(obj.modifyDate,true)
});

vueContentBeforeCreate = () => {
	[regularDataSelect, availableDataSelect, isOpen] = [
		[{value: 'true', label: regulars[1]},{value: 'false', label: regulars[0]}],
		[{value: 'true', label: availables[1]},{value: 'false', label: availables[0]}],
		getOpenStatus()
	];
};

setFormDataObject({id: -1, word: '', replacement: '', regular: false, available: true});

[queryFormItemName, queryFormItemKey, queryFormItemType] = [
	['ID','敏感词','替换词','是否正则表达式','可用状态'],
	['id','word','replacement','regular','available'],
	['string','string','string','select#regularDataSelect','select#availableDataSelect']
];

setFormRulesObject({
	'word': [{trigger: 'blur',type: 'string', required: true}],
	'replacement': [{trigger: 'blur',type: 'string', required: true}]
});

vueContentObject = new Vue(initializeContentOptions());

function getOpenStatus() {
	let isOpen = false;
	$.igety('getOpen', (data) => isOpen = data.open);
	return isOpen;
}

function turnFilter(status) {
	$.iposty('open', {isOpen: status});
}
