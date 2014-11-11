<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
       <html>
       <head>
      <meta charset="utf-8">
      <title>Quiz</title>
      <link rel="icon" type="image/png" href="<spring:url value='/resources/images/quizsmall.png' htmlEscape='true'/>">
      <link type="text/css" rel="stylesheet" href="<spring:url value='/resources/quiz.css' htmlEscape='true'/>"
     media="screen and (min-width: 481px)">
     <link type="text/css" rel="stylesheet" href="<spring:url value='/resources/quiz.css' htmlEscape='true'/>"
     media="screen and (max-width: 480px)">
      <link type="text/css" rel="stylesheet" href="<spring:url value='/resources/quiz.css' htmlEscape='true'/>" media="print">
      <script src="<spring:url value='/resources/js/jquery-1.11.1.min.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/jquery-ui.min.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/sockjs-0.3.4.min.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/stomp.min.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/progressbar.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/jquery.timer.js' htmlEscape='true'/>"></script>
      <script src="<spring:url value='/resources/js/websocketJoin.js' htmlEscape='true'/>"></script>
      </head>
      <body>
	<div id="header">
      <h1><img src="<spring:url value='/resources/images/quizsmall.png' htmlEscape='true'/>" alt="Quiz Icon" height="60" width="60"></h1><br>
    </div>