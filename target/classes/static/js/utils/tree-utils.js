var $tree = $('div#treeContainer');

/**
 * 初始化并构建树对象实例
 * @param sourceData - root根节点数据
 * @param options - 可选参数(包括根节点数据)
 */
function buildTree(sourceData, options, tree) {
	//允许自定义tree对象引用
	if (tree) $tree = $(tree);
	//在div treeContainer中使用根节点初始化tree
	if (options) $tree.treeview(options); 
	else $tree.treeview({data: sourceData});
	//注册树节点展开事件，当展开时候自动到后台加载其下级子节点数据
	if (!options || (options && !options.onNodeSelected)) {
		$tree.on('nodeExpanded', function(event, data) {
			treeNodeExpanded(event, data);
		});
	}
	//注册树节点被选中事件，当节点被选中时根据条件自动刷新右侧数据表格中的数据
	if (!options || (options && !options.onNodeExpanded)) {
		$tree.on('nodeSelected', function(event, data) {
			treeNodeSelected(event, data);
		});
	}
	//初始化后自动展开根目录下一级目录
	$tree.treeview('expandNode', [0]);
}

/**
 * 根据输入数据生成并返回拼接好的树节点
 * PS: 具体实现需要根据具体业务进行重写
 */
var generateNode = function(data) {
	var content = {text: parseNodeText(data), id: data.id};
	if (data.countOfChildren > 0) content.nodes = [];
	return content;
}

/**
 * 根据输入数据解析并返回树节点text的显示内容
 * PS: 具体实现需要根据具体业务进行重写
 */
var parseNodeText = function(data) {
	return data.name ? data.name : data;
}


/**
 * 树节点选中事件
 * PS: 具体实现需要根据具体业务进行重写
 * @param event - 树选中事件对象
 * @param data - 树的数据对象
 */
var treeNodeSelected = function(event, data) {
	if (data.nodeId == 0) {
		//root node被选中,获取所有栏目数据
		initPageableDataByCondition(0,true,globalSize);
	} else {
		//非root node被选中，获取其下所有栏目数据
		initPageableDataByCondition(0,true,globalSize,{'parentId': data.id});
	}
}

/**
 * 树节点扩展事件
 * PS: 具体实现需要根据具体业务进行重写
 * @param event - 树选中事件对象
 * @param data - 树的数据对象
 */
var treeNodeExpanded = function(event, data) {
	if (data.nodes.length == 0) initTreeData(data.id, data);
}

/**
 * 向树根或某个节点下增加子节点
 * @param data - 增加子节点的数据, 格式为[parentNode.nodeId, {node: nodes}]
 */
var addNode = function(data) {
	$tree.treeview('addNode', data);
}
