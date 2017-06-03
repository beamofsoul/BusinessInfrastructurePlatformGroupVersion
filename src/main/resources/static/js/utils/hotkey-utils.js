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
	hotkey('alt+q', function() {getVueRefObject('vueQueryFormVisible').value = String(parseInt(getVueRefObject('vueQueryFormVisible').value) * -1)});
	//弹出与关闭addForm
	hotkey('alt+a', function() {if(!vueContentObject.vueAddModalVisible) vueContentObject.doAddButton(); else vueContentObject.defaultVueBindModalAddData = false});
	//弹出addForm后，进行自动提交表单
	hotkey('alt+s', function() {if(vueContentObject.vueAddModalVisible) vueContentObject.submitAddForm()});
	//弹出addForm后，进行表单重置
	hotkey('alt+r', function() {if(vueContentObject.vueAddModalVisible) resetVueFormData('vueAddForm')});
	//回归页面顶部
	hotkey('alt+b', function() {$('.ivu-back-top').click()});
	//数据表格上一页
	hotkey('alt+n', function() {$('.ivu-page-prev').click()});
	//数据表格下一页
	hotkey('alt+m', function() {$('.ivu-page-next').click()});
	//数据表格第一页
	hotkey('alt+,', function() {vueContentObject.vueCurrentPage = 1;vueContentObject.doPageTurning(1)});
	//数据表格最后一页
	hotkey('alt+.', function() {var pageFinal = Math.ceil(vueContentObject.vueRecordTotal / vueContentObject.vuePageSize);vueContentObject.vueCurrentPage = pageFinal;vueContentObject.doPageTurning(pageFinal)});
});
