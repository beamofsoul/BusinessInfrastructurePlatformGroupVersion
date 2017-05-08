function warnWithAll(ttl,msg,func) {
	if (isFunc(func)) {
		AMUI.dialog.alert({title:ttl,content:msg,onConfirm:func});
	} else {
		console.log(func + '不是有效的方法');
	}
}
function warnWithTtlAndMsg(ttl,msg) {
	warnWithAll(ttl,msg,function(){});
}
function warnWithMsg(msg) {
	warnWithTtlAndMsg('提示信息',msg);
}
function warn() {
	switch(arguments.length) {
	case(0): warnWithMsg(''); break;
	case(1): warnWithMsg(arguments[0]); break;
	case(2): warnWithTtlAndMsg(arguments[0],arguments[1]); break;
	case(3): warnWithAll(arguments[0],arguments[1],arguments[2]); break;
	default: console.log('非法的方法调用: 过多的输入参数');
	}
};
function askWithAll(ttl,msg,yesText,noText,yesFunc,noFunc) {
	if (isNotValidFunc(yesFunc)) return;
	if (isNotValidFunc(noFunc)) return;
	AMUI.dialog.confirm({
		title : ttl,
		content : msg,
		confirmText: yesText,
		cancelText: noText,
		onConfirm : function() {yesFunc();},
		onCancel : function() {noFunc();}
	});
}
function askWithMsgAndBothTextsAndBothFuncs(msg,yesText,noText,yesFunc,noFunc) {
	askWithAll('确认信息',msg,yesText,noText,yesFunc,noFunc);
}
function askWithMsgAndBothTextsAndYesFunc(msg,yesText,noText,yesFunc) {
	askWithMsgAndBothTextsAndBothFuncs(msg,yesText,noText,yesFunc,function(){});
}
function askWithMsgAndBothFuncs(msg,yesFunc,noFunc) {
	askWithMsgAndBothTextsAndYesFunc(msg,null,null,yesFunc);
}
function askWithMsgAndYesFunc(msg,yesFunc) {
	askWithMsgAndBothFuncs(msg,yesFunc,function(){});
}
function ask() {
	switch(arguments.length) {
	case(0): console.log('非法的方法调用: 缺少必要的输入参数'); break;
	case(1): console.log('非法的方法调用: 过少的输入参数'); break;
	case(2): askWithMsgAndYesFunc(arguments[0],arguments[1]); break;
	case(3): askWithMsgAndBothFuncs(arguments[0],arguments[1],arguments[2]); break;
	case(4): askWithMsgAndBothTextsAndYesFunc(arguments[0],arguments[1],arguments[2],arguments[3]); break;
	case(5): askWithMsgAndBothTextsAndBothFuncs(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]); break;
	case(6): askWithAll(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]); break;
	default: console.log('非法的方法调用: 过多的输入参数');
	}
}
function isFunc(func) {
	return typeof(func) == 'function';
}
function isNotFunc(func) {
	return !isFunc(func);
}
function isNotValidFunc(func) {
	if (isNotFunc(func)) {
		console.log(func + '不是有效的方法');
		return true;
	} else {
		return false;
	}
}
