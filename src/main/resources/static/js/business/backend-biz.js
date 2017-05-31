loadTreeRootUrl = 'permission/single';
loadTreeRootDataFunction = () => {id: 1}
loadTreeNodeUrl = 'permission/children';

let vueContentElementSelector = '#contentContainer';
let vueContentData = {treeData: generateRootNode()};
let vueContentComputed = {};
let vueContentMethods = {toggleExpand: toggleExpand, selectChange: selectChange, checkChange: checkChange};

function initializeContentOptions() {
	return {el: vueContentElementSelector, data: vueContentData, computed: vueContentComputed, methods: vueContentMethods};
}

vueContentObject = new Vue(initializeContentOptions());
vueContentObject.$refs.tree.$children[0].handleExpand(toggleExpand);