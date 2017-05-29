$(function() {
	//.ajaxError事件定位到document对象，文档内所有元素发生ajax请求异常
	//都将冒泡到document对象的ajaxError事件执行处理
	$(document).ajaxError(
		function(event, xhr, options, exc) {
			if (xhr.status == 'undefined') {
				return;
			}
			let duration = 5;
			let error = '';
			switch (xhr.status) {
			case 403:
				error = '系统拒绝：您没有访问权限。';
				break;
			case 404:
				error = '您访问的资源不存在。';
				break;
			case 500:
				error = '系统内部错误，请联系管理员。';
				break;
			default:
				error = '未知系统错误，请联系管理员。';
			}
			toastError(error,duration);
		});
});