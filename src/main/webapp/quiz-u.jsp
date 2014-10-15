
<%@ include file='header.jsp'%>
<h2>Take Quiz</h2>

<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
	<br>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
<p id="positivemessage" class="positivemessage">${reqPositiveMessage}</p>
</c:if>

<c:if test="${allPlayersFound}">
<script>connect(${game.gameId});</script>
<p id="confirmationMessage" class="positivemessage"> </p>
</c:if>


<form action='<%=response.encodeURL("answerQuestion-u.action")%>' method='POST'>

	<table class="questionTable">

		<tr>
			<td> <b>Question:</b><br>
			${sessionScope.question.question}</td>
		</tr>
		<br>
		
		<tr>
		<td><b>Answer</b> <br> 
		<input type="radio" name="option" value="0">${sessionScope.question.option1}<br>
			<input type="radio" name="option" value="1">${sessionScope.question.option2}<br>
			<input type="radio" name="option" value="2">${sessionScope.question.option3}<br>
			<input type="radio" name="option" value="3">${sessionScope.question.option4}<br></td>
		</tr>

		<tr>
			<td>&nbsp</td>
		</tr>


		<tr>
			<td colspan="2"><br> <input type="submit" name="submit"
				value="Submit"></td>
		</tr>

	</table>

</form>
<br>
		
<c:import url="footer.jsp" />
</body>
</html>