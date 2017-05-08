<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="sizes" type="java.lang.String" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="delim" value="," />
<c:set var="array" value="${fn:split(sizes, delim)}" />

<div id="pageButtons" class="am-g">
	<div class="am-u-sm-6">
		显示第
		<dfn id="firstRecord"></dfn>
		到第
		<dfn id="lastRecord"></dfn>
		条记录，总共
		<dfn id="totalRecords"></dfn>
		条记录，每页显示
		<div class="am-dropdown am-dropdown-up" data-am-dropdown>
			<dfn title="click it" id="howManyRecords" class="am-dropdown-toggle" data-am-dropdown-toggle></dfn>
			<ul id="selectRecordNumber" class="am-dropdown-content">
				<c:forEach items="${array }" var="item">
					<li>
						<a href="javascript:initPageableData(0,true,${item })">
							<dfn id="firstRecordNumber">${item }</dfn>
						</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		条记录
	</div>
	<ul class="am-u-sm-6 pagination am-pagination" style="text-align:right; margin: 0px;"></ul>
</div>
