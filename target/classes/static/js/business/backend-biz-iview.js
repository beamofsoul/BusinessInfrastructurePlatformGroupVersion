loadTreeRootUrl = 'permission/single';
loadTreeRootDataFunction = function() {return {id: 1}}
loadTreeNodeUrl = 'permission/children';

var vueContentElementSelector = '#contentContainer';
var vueContentData = {treeData: generateRootNode()};
var vueContentComputed = {};
var vueContentMethods = {toggleExpand: toggleExpand, selectChange: selectChange, checkChange: checkChange, getCheckedNodes: getCheckedNodes, getSelectedNodes: getSelectedNodes};

function initializeContentOptions() {
	return {el: vueContentElementSelector, data: vueContentData, computed: vueContentComputed, methods: vueContentMethods};
}

var vueContentObject = new Vue(initializeContentOptions());

$(function() {
	//初始化树根节点下级子节点数据，并展开根节点下级子节点
	toggleExpand(vueContentObject.treeData[0]);
	vueContentObject.$refs.tree.$children[0].handleExpand(toggleExpand);
});

generateBreadcrumbItem = function() {
	return '<span><span class="ivu-breadcrumb-item-link">首页内容</span></span>';
}