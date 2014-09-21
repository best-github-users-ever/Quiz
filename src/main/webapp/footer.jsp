<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.quiz.utility.Utility"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<c:if test="${not empty user}">
<div class="homepage">
    <s:url var="homeURL" action="request-topics-u.action" >  </s:url>  
	Click <a href="<s:property value="#homeURL"/>">
		here</a> to go to topics page.
</div>
<br>
</c:if>

<c:if test="${empty user}">
<div class="login">
    <s:url var="loginURL" action="login-again.action" >  </s:url>  
	Click <a href="<s:property value="#loginURL"/>">
		here</a> to go to login page.
</div>
<br>
</c:if>
<br>
<c:if test="${not empty user}">
	<div class="userstatus">
		<b>User <s:property value="#session.user.userId" /> is logged in.</b> 
        <s:url var="logoutURL" action="logout-u.action" >  </s:url>  
	    (Click <a href="<s:property value="#logoutURL"/>"> here</a> to
		logout)
	<br>
	<br>
	</div>

	
</c:if>

<div class="footerdeveloper">Developed by: Team Rocket</div>

<div class="footerdate">
	Current Date/Time:
	<%=Utility.getFooterDate()%></div>
<%-- <%@ include file='debug.jsp'%> --%>
	