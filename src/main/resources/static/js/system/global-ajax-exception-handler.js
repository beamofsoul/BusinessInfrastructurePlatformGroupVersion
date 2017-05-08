$(function() {
	//.ajaxError事件定位到document对象，文档内所有元素发生ajax请求异常
	//都将冒泡到document对象的ajaxError事件执行处理
	$(document).ajaxError(
		function(event, xhr, options, exc) {
			if (xhr.status == 'undefined') {
				return;
			}
			switch (xhr.status) {
			case 403:
				warn("系统拒绝：您没有访问权限。");
				break;
			case 404:
				warn("您访问的资源不存在。");
				break;
			case 500:
				warn("系统内部错误，请联系管理员。");
				break;
			default:
				warn("未知系统错误，请联系管理员。");
			}
		});
});