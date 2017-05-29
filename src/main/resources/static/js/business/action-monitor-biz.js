if (vueContentObject) getVueObject().$destroy();

const levels = ['毁灭性的','极恶性的','恶性的','良性的','可忽略的'];
[loadPageableDataUrl, tableColumnsName, tableColumnsKey] = [
	'actionMonitorsByPage',
	['','ID','用户','行为','目标','影响','危害级别','IP地址','MAC地址','具体时间'],
	['selection','id','user','specificAction','target','effect','hazardLevel','ipAddress','macAddress','createDate']
];

parseValuesOnTableEachRow = obj => ({
	id: obj.id,
	user: obj.user.nickname,
	specificAction: obj.specificAction,
	target: obj.target,
	effect: obj.effect,
	hazardLevel: levels[obj.hazardLevel],
	ipAddress: obj.ipAddress,
	macAddress: obj.macAddress,
	createDate:formatDate(obj.createDate)
});

vueContentBeforeCreate = () => {
	hazardLevelDataSelect = [{value: '0', label: levels[0]},{value: '1', label: levels[1]},{value: '2', label: levels[2]},{value: '3', label: levels[3]},{value: '4', label: levels[4]}];
};

[queryFormItemName, queryFormItemKey, queryFormItemType] = [
	['ID','用户','行为','目标','影响','危害级别','IP地址','MAC地址'],
	['id','user','specificAction','target','effect','hazardLevel','ipAddress','macAddress'],
	['string','string','string','string','string','select#hazardLevelDataSelect','string','string']
];

vueContentObject = new Vue(initializeContentOptions());