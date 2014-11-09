<%@ include file='header.jsp'%>
<h2>Take The Quiz</h2>

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

		<div id="questionTable" class="questionTable" hidden="true">
			<p id="countdown"></p>

			<p id="questionNumber" class="content-header">Question</p>
			<br>

			<p id="questionId" hidden="true"></p>
			<p id="question-text"></p>
          <div id="allOptions" hidden="true">
			<p id="answer-label">Possible Answers</p>
			<br> <input type="image" id="option-a-button"
                class="option-button" src="resources/images/quizsmall.png" alt="A"
				onclick="makeSelection(${game.gameId}, '${sessionScope.user.userId}', 0);">
			<div id="option-a" class="options">option A text that extends
				over several lines of type several lines I say</div>

			<input type="image" id="option-b-button"
                class="option-button" src="resources/images/quizsmall.png" alt="B"
				onclick="makeSelection(${game.gameId}, '${sessionScope.user.userId}', 1);">
			<div id="option-b" class="options">option b text</div>

			<input type="image" id="option-c-button"
                class="option-button" src="resources/images/quizsmall.png" alt="C"
				onclick="makeSelection(${game.gameId}, '${sessionScope.user.userId}', 2);">
			<div id="option-c" class="options">option C text</div>

			<input type="image" id="option-d-button"
                class="option-button" src="resources/images/quizsmall.png" alt="D"
				onclick="makeSelection(${game.gameId}, '${sessionScope.user.userId}', 3);">
			<div id="option-d" class="options">option D text</div>
          </div>
		</div>
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
</div>

<br>
<p id="JSESSIONID" hidden="true">${sessionScope.JSESSIONID }</p>

<c:import url="footer.jsp" />

</body>
</html>