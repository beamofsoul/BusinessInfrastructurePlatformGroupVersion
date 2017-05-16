/**
 * 加载树控件根节点需要提供的数据
 */
var loadTreeRootUrl = 'permission/single';
var loadTreeRootDataFunction = function() {return {id: 1}}
var loadTreeRootCallback = function(content, data) {
	data.obj.countOfChildren = 1;
	content.push(parseNode(data.obj));
};

/**
 * 加载树控件根节点下子节点需要提供的数据
 */
var loadTreeNodeUrl = 'permission/children';
var loadTreeNodeDataFunction = function(parent) {return {condition: {parentId: parent.id}}}
var loadTreeNodeCallback = function(content, data) {
	var children = data.children;
	for(var r in children) content.push(parseNode(children[r]));
}
/**
 * 将输入数据解析成树控件node节点
 */
function parseNode(data) {
	return {id: data.id, expand: data.expand, title: data.name, children: data.countOfChildren != 0 ? [{}] : null};
}

/**
 * 生成并返回生成的树控件根节点 
 */
function generateRootNode() {
	var content = [];
	$.posty(loadTreeRootUrl,loadTreeRootDataFunction(),function(data) {
		loadTreeRootCallback(content, data);
	});
	return content;
}

/**
 * 根据输入的父节点加载其下子节点
 * @param parent 父节点
 */
function toggleExpand(parent) {
	var content = [];
	$.posty(loadTreeNodeUrl,loadTreeNodeDataFunction(parent),function(data) {
		loadTreeNodeCallback(content,data);
	});
	parent.children = content;
}

/**
 * 当树控件任何节点被点选中时发生的事件
 * @param node 被点选的节点
 */
function selectChange(node) {
	alert('selectChange: ' + JSON.stringify(node));
}

/**
 * 当树控件任何节点前复选框被选中时发生的事件
 * @param nodes 一个到多个被点选中复选框的节点集合
 */
function checkChange(nodes) {
	alert('checkChange: ' + JSON.stringify(nodes));
}

/**
 * 获取被鼠标选中的节点(非复选框被勾选)
 * @param nodes 被选中的节点集合
 */
function getSelectedNodes(nodes) {
	alert('selectedNodes: ' + JSON.stringify(nodes));
}

/**
 * 获取复选框被选中的节点集合
 * @param nodes 被选中复选框的节点集合
 */
function getCheckedNodes(nodes) {
	alert('checkedNodes: ' + JSON.stringify(nodes));
}
