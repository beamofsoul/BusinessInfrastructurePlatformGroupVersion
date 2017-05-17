var vueElementSelector = '.layout';
var vueData = {spanLeft: 3, spanRight: 21, breadcrumb: [{href: '#', content: '首页内容'}]};
var vueComputed = {iconSize: getIconSize};
var vueMethods = {toggleClick: toggleMenu, contentClick: loadContent};

function getIconSize() {
	return this.spanLeft === 3 ? 18 : 24;
}

function toggleMenu () {
	setGridScale(this.spanLeft === 3 ? 1 : 3, this);
}

function setGridScale(left,element) {
	element.spanLeft = left;
	element.spanRight = 24 - left;
}

function initBreadcrumbContent(name) {
	if (name === 'adminIndexContent') vueObject.breadcrumb = [{href: '#', content: '首页内容'}];
	if (name.indexOf('permission/') == 0) vueObject.breadcrumb = [{href: '#', content: '权限管理'}];
}

function loadContent(name) {
	initBreadcrumbContent(name);
	initCurrentRequestMappingRootPath(name);//设置当前点击path所属的requestMapping 模块名
	if (name.indexOf('logout') != -1) $(window).attr('location',name);
	else $('.layout-content-main').load(name);
}

function initializeOptions() {
	return {el: vueElementSelector, data: vueData, computed: vueComputed, methods: vueMethods};
}

var vueObject = new Vue(initializeOptions());

loadContent('adminIndexContent');


