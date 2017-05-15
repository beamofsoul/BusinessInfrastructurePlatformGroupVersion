/** 初始化vueContentObject之后 可自定义操作  **/
$(document).ready(function(){
	//伸出与收缩queryForm
	hotkey('space+q', function() {getVueRefObject('defaultVueBindCollapseQueryFormData').value = String(parseInt(getVueRefObject('defaultVueBindCollapseQueryFormData').value) * -1)});
	//弹出与关闭addForm
	hotkey('space+a', function() {if(!vueContentObject.defaultVueBindModalAddData) vueContentObject.vueBindButtonHeadAddMethod(); else vueContentObject.defaultVueBindModalAddData = false});
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