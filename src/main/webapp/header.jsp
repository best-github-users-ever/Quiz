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
      <link rel="icon" type="image/png" href="resources/images/quizsmall.png">
      <link type="text/css" rel="stylesheet" href="resources/quiz.css"
     media="screen and (min-width: 481px)">
     <link type="text/css" rel="stylesheet" href="resources/quiz.css"
     media="screen and (max-width: 480px)">
      <link type="text/css" rel="stylesheet" href="resources/quiz.css" media="print">
      <script src="resources/js/jquery-1.11.1.min.js"></script>
      <script src="resources/js/sockjs-0.3.4.min.js"></script>
      <script src="resources/js/stomp.js"></script>
      <script src="resources/js/progressbar.js"></script>
      <script src="resources/js/jquery.timer.js"></script>
      <script src="resources/js/websocketJoin.js"></script>
 	  
      </head>
      <body>
	<div id="header">
      <h1><img src="resources/images/quizsmall.png" alt="Quiz Icon" height="60" width="60"></h1><br>
    </div>