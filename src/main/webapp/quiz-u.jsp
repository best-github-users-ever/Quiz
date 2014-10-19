
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
<c:if test="${not empty gameFound}">
	<script>connect(${game.gameId},'${sessionScope.user.userId}');</script>
</c:if>

<p id="confirmationMessage" class="positivemessage"></p>
<p id="confirmationMessage2" class="positivemessage"></p>

<form action='<%=response.encodeURL("ready-u.action")%>' method='POST'>
	<input type="submit" name="submit" id="readyButton"
		class="centeredButton" value="Click When Ready to Start Game">
		
	<c:if test="${empty allPlayersFound}">
		<script>setButtonVisible(false);</script>
	</c:if>
	<c:if test="${not empty allPlayersFound}">
		<script>setButtonVisible(true);</script>
	</c:if>
</form>

<c:if test="${not empty sessionScope.question }">

	<form action='<%=response.encodeURL("answerQuestion-u.action")%>'
		method='POST'>

		<table class="questionTable">

			<tr>
				<td><b>Question:</b><br> ${sessionScope.question.question}</td>
			</tr>

			<tr>
				<td><b>Answer</b> <br> <input type="radio" name="option"
					value="0">${sessionScope.question.option1}<br> <input
					type="radio" name="option" value="1">${sessionScope.question.option2}<br>
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
</c:if>

<br>

<c:import url="footer.jsp" />
</body>
</html>