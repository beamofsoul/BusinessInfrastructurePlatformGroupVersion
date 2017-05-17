function hotkey(keys,fn) {
	hotkeys(keys, function(event,handler){if(fn) fn();event.preventDefault()});
}

(function() {
	var k = function(action){
		var eventObj = document.createEvent("Events");
		eventObj.initEvent("keydown", true, true);
		eventObj.keyCode = 75;
		eventObj.which = 75;
		document.body.dispatchEvent(eventObj);
	};

	var killSpaceBar = function(evt) {

	var target = evt.target || {},
		isInput = ("INPUT" == target.tagName || "TEXTAREA" == target.tagName || "SELECT" == target.tagName || "EMBED" == target.tagName);
	
		// if we're an input or not a real target exit
		if(isInput || !target.tagName) return;
	
		// if we're a fake input like the comments exit
		if(target && target.getAttribute && target.getAttribute('role') === 'textbox') return;
	
		// ignore the space and send a 'k' to pause
		if (evt.keyCode === 32) {
			evt.preventDefault();
			k();
		}
	};

	document.addEventListener("keydown", killSpaceBar, false);
})();

$(document).ready(function(){
	//伸出与收缩queryForm
	hotkey('space+q', function() {getVueRefObject('defaultVueBindCollapseQueryFormData').value = String(parseInt(getVueRefObject('defaultVueBindCollapseQueryFormData').value) * -1)});
	//弹出与关闭addForm
	hotkey('space+a', function() {if(!vueContentObject.vueAddModalVisible) vueContentObject.vueBindButtonHeadAddMethod(); else vueContentObject.defaultVueBindModalAddData = false});
	//弹出addForm后，进行自动提交表单
	hotkey('space+s', function() {if(vueContentObject.vueAddModalVisible) vueContentObject.vueBindButtonHeadAddSubmitMethod()});
	//弹出addForm后，进行表单重置
	hotkey('space+r', function() {if(vueContentObject.vueAddModalVisible) resetVueFormData('defaultVueBindFormAddData')});
	//回归页面顶部
	hotkey('space+b', function() {$('.ivu-back-top').click()});
	//数据表格上一页
	hotkey('space+n', function() {$('.ivu-page-prev').click()});
	//数据表格下一页
	hotkey('space+m', function() {$('.ivu-page-next').click()});
	//数据表格第一页
	hotkey('space+,', function() {vueContentObject.defaultVueBindPageCurrentData = 1;vueContentObject.vueBindPageOnChangeMethod(1)});
	//数据表格最后一页
	hotkey('space+.', function() {var pageFinal = Math.ceil(vueContentObject.defaultVueBindPageTotalData / vueContentObject.defaultVueBindPageSizeData);vueContentObject.defaultVueBindPageCurrentData = pageFinal;vueContentObject.vueBindPageOnChangeMethod(pageFinal)});
});
