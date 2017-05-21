//项目根路径
var projectRootPath = window.document.location.pathname.substring(0,window.document.location.pathname.substr(1).indexOf('/')+1);
//当前请求映射根路径
var currentRequestMappingRootPath;

/**
 * 初始化当前请求映射根路径，在点击菜单加载子页面时使用
 * @param currentRequestPath 当前请求路径，例如: user/usersByPage
 */
function initCurrentRequestMappingRootPath(currentRequestPath){
	currentRequestMappingRootPath = (!currentRequestPath) ? null : currentRequestPath.split('/')[0];
}

var arrayContains = function (array, value) {
	for (i in array) {
		if (array[i] == value) return true;
	}
	return false;
}