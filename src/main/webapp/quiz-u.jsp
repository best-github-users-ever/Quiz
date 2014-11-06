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
					<p id="questionNumber"></p>
					<p id="questionId" hidden="true"></p>
					<p id="question"></p></td>
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
				<td colspan="2"><br> <input id="submitButton"
					type="submit" name="submit" value="Submit"
					onclick="answerSubmitted(${game.gameId},'${sessionScope.user.userId}');"></td>
			</tr>

			<tr>
				<td>&nbsp</td>
			</tr>
			<tr>
				<td><label>Time Remaining: </label><span id="countdown"></span></td>
			</tr>

		</table>
	</div>

	<div id="content-upper-right">
		<p class="content-header">Player Scores</p>

		<div class="my-progressbar-container">

			<div id="my-progressbar-text1" class="progressbar-text top-left1"></div>
			<div id="my-progressbar1"></div>

			<div id="my-progressbar-text2" class="progressbar-text top-left2"></div>
			<div id="my-progressbar2"></div>

			<div id="my-progressbar-text3" class="progressbar-text top-left3"></div>
			<div id="my-progressbar3"></div>

			<div id="my-progressbar-text4" class="progressbar-text top-left4"></div>
			<div id="my-progressbar4"></div>

			<div id="my-progressbar-text5" class="progressbar-text top-left5"></div>
			<div id="my-progressbar5"></div>
		</div>

	</div>
	<div id="content-lower-right">
		<p class="content-header">Chat</p>
		<select id="opponentSelectList" class="opponentSelectList" multiple
			size="5">
		</select>
		<div id="messages"></div>

		<textarea rows="2" wrap="soft" id="chatbox"></textarea>
		<div id="sendButtonContainer">
			<br> <input type="button" name="send" id="chatButton"
				value="Send" onclick="sendPressed(${game.gameId});">
		</div>
	</div>

	<!--</div>-->
	<br>
	<p id="JSESSIONID" hidden="true">${sessionScope.JSESSIONID }</p>

	<c:import url="footer.jsp" />

	</body>
	</html>