<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="include" type="java.lang.String" required="false"%>
<%@ attribute name="exclude" type="java.lang.String" required="false"%>
<%@ attribute name="security" type="java.lang.String" required="false"%>
<%@ attribute name="search" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="toolbar" class="am-g">
	<div class="am-u-sm-12 am-u-md-6">
		<div class="am-btn-toolbar">
	     	<div class="am-btn-group am-btn-group-xs">
	     		<c:if test="${empty include || include == ''}">
					<c:set var="include" value="invert,add,update,delete"></c:set>	     			
	     		</c:if>
				<c:if test="${fn:contains(include, 'invert') and not fn:contains(exclude, 'invert')}">
					<button type="button" class="am-btn am-btn-default" id="invertButton"><span class="am-icon-check-square"></span> 反选 </button>
				</c:if>
				<c:if test="${fn:contains(include, 'add') and not fn:contains(exclude, 'add')}">
					<c:choose>
						<c:when test="${not empty security && security != '' }">
							<sec:authorize access="hasPermission('${security }','${security }:add')">
								<button type="button" class="am-btn am-btn-default" id="addButton"><span class="am-icon-plus"></span> 新增 </button>
							</sec:authorize>
						</c:when>
						<c:otherwise>
							<button type="button" class="am-btn am-btn-default" id="addButton"><span class="am-icon-plus"></span> 新增 </button>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${fn:contains(include, 'update') and not fn:contains(exclude, 'update')}">
					<c:choose>
						<c:when test="${not empty security && security != '' }">
							<sec:authorize access="hasPermission('${security }','${security }:update')">
								<button type="button" class="am-btn am-btn-default" id="updateButton"><span class="am-icon-save"></span> 修改 </button>
							</sec:authorize>
						</c:when>
						<c:otherwise>
							<button type="button" class="am-btn am-btn-default" id="updateButton"><span class="am-icon-save"></span> 修改 </button>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${fn:contains(include, 'delete') and not fn:contains(exclude, 'delete')}">
					<c:choose>
						<c:when test="${not empty security && security != '' }">
							<sec:authorize access="hasPermission('${security }','${security }:delete')">
								<button type="button" class="am-btn am-btn-default" id="deleteButton"><span class="am-icon-trash-o"></span> 删除 </button>
							</sec:authorize>
						</c:when>
						<c:otherwise>
							<button type="button" class="am-btn am-btn-default" id="deleteButton"><span class="am-icon-trash-o"></span> 删除 </button>
						</c:otherwise>
					</c:choose>
				</c:if>	     		
			</div>
		</div>
	</div>
	
	<c:if test="${not empty search && search}">
		<div class="am-u-sm-12 am-u-md-3">
			<div class="am-input-group am-input-group-sm">
				<input type="text" id="searchInList" class="am-form-field">
				<span class="am-input-group-btn">
					<button class="am-btn am-btn-default" onclick="onSearch(this)" type="button">搜索</button>
				</span>
			</div>
		</div>
	</c:if>
</div>