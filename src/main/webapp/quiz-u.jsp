
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

<input type="button" name="submit" id="readyButton"
	class="centeredButton" value="Click When Ready to Start Game"
	onclick="readyPressed(${game.gameId},'${sessionScope.user.userId}');">

<p id="confirmationMessage" class="positivemessage"></p>
<p id="errorMessage" class="errortext"></p>

<!--<div id="wrapper">-->
<div id="content">
	<div id="content-left">
		<p class="content-header">Game Updates</p>
		<p id="updateMessage" class="updatemessage"></p>
	</div>
	<div id="content-main">

		<c:if test="${empty allPlayersFound}">
			<script>setReadyButtonVisiblity(false);</script>
		</c:if>
		<c:if test="${not empty allPlayersFound}">
			<script>setReadyButtonVisiblity(true);</script>
		</c:if>

		<table id="questionTable" class="questionTable" hidden="true">

			<tr>
				<td><p class="content-header">Question</p> <br>
					<p id="question"></p>
					<p id="questionId" hidden="true"></p></td>
			</tr>

			<tr>
				<td><b>Answer</b> <br> <input type="radio" name="option"
					value="0"><label id="ansOpt1"></label> <br> <input
					type="radio" name="option" value="1"><label id="ansOpt2"></label>
					<br> <input type="radio" name="option" value="2"><label
					id="ansOpt3"></label> <br> <input type="radio" name="option"
					value="3"><label id="ansOpt4"></label> <br>
			</tr>

			<tr>
				<td>&nbsp</td>
			</tr>


			<tr>
				<td colspan="2"><br> <input type="submit" name="submit"
					value="Submit"
					onclick="answerSubmitted(${game.gameId},'${sessionScope.user.userId}');"></td>
			</tr>

		</table>
	</div>
	<div id="content-upper-right">
		<p class="content-header">Player Scores</p>
		<div id="playerList" class="playerList" hidden="true">
		</div>
	</div>
	<div id="content-lower-right">
		<p class="content-header">Chat</p>
		<select id="opponentSelectList" class="opponentSelectList" multiple
			size="2"></select>
	</div>
</div>
<!--</div>-->
<br>
<p id="JSESSIONID" hidden="true">${sessionScope.JSESSIONID }</p>

<c:import url="footer.jsp" />
</body>
</html>