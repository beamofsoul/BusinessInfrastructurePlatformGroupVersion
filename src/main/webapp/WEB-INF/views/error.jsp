<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="include_taglib.html"%>
<!DOCTYPE HTML>
<html>
<head>
<title>ERROR PAGE</title>
</head>
<body>
	<h1>ERROR - ${status }</h1>
	<hr>
	<ul>
		<li><span style="font-family: cursive;"> <c:choose>
					<c:when test="${status == 403 }">
    				You have no permissions to access the resource that you expect
    			</c:when>
					<c:when test="${status == 404 }">
    				Could not find the page that you want to access
    			</c:when>
					<c:when test="${status == 500 }">
    				An ${exception } occurred
    			</c:when>
					<c:otherwise>
    				An unknown exception or error occurred
    			</c:otherwise>
				</c:choose>
		</span></li>
		<li><span style="font-family: cursive;">via a request with
				URL ${path }</span></li>
		<li><span style="font-family: cursive;">at ${timestamp }</span></li>
		<br/>
		<li><span style="font-family: cursive;">Please access <a
				href="${pageContext.request.contextPath }/index">index page</a> or
				try the current operation later...
		</span></li>
	</ul>
</body>
</html>
