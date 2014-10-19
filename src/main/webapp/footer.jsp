<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.quiz.utility.Utility"%>

<c:if test="${not empty sessionScope.user}">
<div class="homepage">
    <c:url var="homeURL" value="/request-topics-u.action" >  </c:url>  
	Click <a href="${homeURL}">
		here</a> to go to topics page.
</div>
<br>
</c:if>

<c:if test="${empty sessionScope.user}">
<div class="login">
    <c:url var="loginURL" value="/login-again.action" >  </c:url>  
	Click <a href="${loginURL}">
		here</a> to go to login page.
</div>
<br>
</c:if>
<br>
<c:if test="${not empty sessionScope.user}">
	<div class="userstatus">
		<b>User ${sessionScope.user.userId} is logged in.</b> 
        <c:url var="logoutURL" value="/logout-u.action" >  </c:url>  
	    (Click <a href="${logoutURL}"> here</a> to
		logout)
	<br>
	<br>
	</div>
<br>
</c:if>

<div class="footerdeveloper">Developed by: Team Rocket</div>

<div class="footerdate">
	Current Date/Time:
	<%=Utility.getFooterDate()%></div>
 <%-- <%@ include file='debug.jsp'%> --%>
	