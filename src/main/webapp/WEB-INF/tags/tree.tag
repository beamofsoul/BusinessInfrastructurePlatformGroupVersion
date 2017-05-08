<%@ tag pageEncoding="UTF-8"%>
<div id="treeContainer" class="am-cf am-padding am-padding-top-0 am-padding-bottom-0" 
	style="border: 1px solid black;">
	<ul class="am-tree" id="dataTree">
		<!-- 包含子级的条目模板 -->
		<li class="am-tree-branch am-hide" data-template="treebranch">
			<div class="am-tree-branch-header">
				<button class="am-tree-branch-name">
  					<span class="am-tree-icon am-tree-icon-folder"></span>
  					<span class="am-tree-label"></span>
				</button>
			</div>
			<ul class="am-tree-branch-children"></ul>
			<div class="am-tree-loader">Loading...</div>
		</li>

		<!-- 不包含子级的条目模板 -->
		<li class="am-tree-item am-hide" data-template="treeitem">
			<button class="am-tree-item-name">
				<span class="am-tree-icon am-tree-icon-item"></span>
				<span class="am-tree-label"></span>
			</button>
		</li>
	</ul>
</div>