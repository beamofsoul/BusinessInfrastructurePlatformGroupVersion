var levels = ['毁灭性的','极恶性的','恶性的','良性的','可忽略的']
var columnNames = ['ID','用户','行为#C80#','目标#C80#','影响','危害级别','IP地址','MAC地址','日期时间'];
var attributeNames = ['id','user','specificAction','target','effect','hazardLevel','ipAddress','macAddress','createDate'];

loadPageableDataUrl = 'actionMonitorsByPage';
loadPageableDataCallback = function(data) {
	data = data.pageableData;
	var value = [];
	for(var i=0;i<data.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.content[i]);
	}
	return generateDefaultDataTable(columnNames,attributeNames,value,true, null);
}

function parseValuesOnEachRow(obj) {
	return [obj.id,
		obj.user.userBaseInfo.name,
		obj.specificAction,
		obj.target,
		obj.effect,
		levels[obj.hazardLevel],
		obj.ipAddress,
		obj.macAddress,
		formatDate(obj.createDate, true)]
}
