<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="primary" type="java.lang.String" required="true"%>
<%@ attribute name="secondary" type="java.lang.String" required="true"%>
<%@ attribute name="delim" type="java.lang.String" required="false"%>
<%@ attribute name="hr" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${empty delim }">
	<c:set var="delim" value="/" />
</c:if>
<div id="headTitle" class="am-cf am-padding am-padding-bottom-0">
	<div class="am-fl am-cf">
		<strong class="am-text-primary am-text-lg">${primary }</strong>
		 ${delim } 
		<small>${secondary }</small>
	</div>	
</div>
<c:if test="${hr }">
	<hr>
</c:if>