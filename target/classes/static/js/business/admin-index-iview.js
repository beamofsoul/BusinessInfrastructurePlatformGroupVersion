var vueElementSelector = '.layout';
var vueData = {spanLeft: 3, spanRight: 21};
var vueComputed = {iconSize: getIconSize};
var vueMethods = {toggleClick: toggleMenu, contentClick: loadContent};

var generateBreadcrumbItem;

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

function loadContent(name) {
	if (name.indexOf('logout') != -1) $(window).attr('location',name);
	else $('.layout-content-main').load(name);
}

function initializeOptions() {
	return {el: vueElementSelector, data: vueData, computed: vueComputed, methods: vueMethods};
}

var vueObject = new Vue(initializeOptions());

function initialzeBreadcrumb() {
	var content = [];
	content.push('<span>');
	content.push('<a class="ivu-breadcrumb-item-link" href="javascript:loadContent(\'adminIndexContent\')">首页</a>');
	content.push('<span class="ivu-breadcrumb-item-separator">/</span>');
	content.push('</span>');
	if (generateBreadcrumbItem) content.push(generateBreadcrumbItem());
	else alert('no generateBreadcrumbItem found...')
	return $('.layout-breadcrumb .ivu-breadcrumb').html(content.join(''));
}

$(function() {
	loadContent('adminIndexContent');
	setTimeout(function(){initialzeBreadcrumb()}, 1000);
});


