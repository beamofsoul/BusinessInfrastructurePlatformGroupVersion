<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE HTML>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/iview/iview.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/static/css/business/admin-index-iview.css">
  </head>
  <body>
    <div class="layout" :class="{'layout-hide-text': spanLeft < 3}">
        <Row type="flex">
        	<i-col :span="spanLeft" class="layout-menu-left">
            	<%@ include file="include_admin_sidebar_iview.html"%>
            </i-col>
            <i-col :span="spanRight">
            	<%@ include file="include_header_iview.html"%>
            	<%@ include file="include_breadcrumb_iview.html"%>
            	<div class="layout-content">
					<div class="layout-content-main">
					</div>
				</div>
                <%@ include file="include_footer_iview.html"%>
            </i-col>
        </Row>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/utils/project-path-utils.js"></script>
	<script src="${pageContext.request.contextPath}/static/iview/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/iview/vue.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/iview/iview.min.js"></script>
	<script src="${pageContext.request.contextPath}/static/js/business/admin-index-iview.js"></script>
	
	<script src="${pageContext.request.contextPath}/static/js/utils/ajax-utils.js"></script>
	</body>
</html>