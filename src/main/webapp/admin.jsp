<%@ include file='header.jsp'%>

<h2>Quiz Administration</h2>
<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>
<div class="adminPage">

	<div id="newTopic" class="adminDiv">
		<form action="/Quiz/new-topic.action" method="POST">
			<div >
				<label><b>Add Topic</b></label><br> <input type="text" id="newTopic" name="name"
					size="30"><br> <input type="submit" value="Add Topic"
					id="addTopicButton">
			</div>
		</form>
	</div>

	<div id="newQuestion" class="adminDiv">
		<br> <br>
		<form action="/Quiz/new-question.action" method="POST">

			<label><b>Add Question</b></b></label><br> <br> 
			<label>Choose Topic for this Question</label><br> 
			<select name="topicId">
				<c:forEach var="topic" items="${topicList}">
					<option value="${topic.topicId}">${topic.name}
				</c:forEach>
			</select> <br> <br> 
			<label>The Question</label><br> <input name="question"
				type="text" id="newQuestion" size="80"><br> <br>
				
				<label>Option
				1</label><br> <input type="text" id="newOption1" size="80"  name="option1"><br>
			<label>Option 2</label><br> <input type="text" id="newOption2"
				size="80" name="option2"><br> <label>Option 3</label><br> <input
				type="text" id="newOption3" size="80"  name="option3"><br> <label>Option
				4</label><br> <input type="text" id="newOption4" size="80"  name="option4"><br>
			<br> <label>Answer is 1, 2, 3 or 4</label><br> <input
				type="radio" name="answerIdx" value="1">1<br> <input
				type="radio" name="answerIdx" value="2">2<br> <input
				type="radio" name="answerIdx" value="3">3<br> <input
				type="radio" name="answerIdx" value="4">4<br> <input
				type="submit" value="Add Question" id="addQuestionButton">
	</div>
	</form>
</div>
<br>
<br>
<c:import url="footer.jsp" />
</body>
<script>
	function hintClick(event) {
		var userId = $("#userId").val();

		if (!userId.match(/^[0-9a-z]+$/)) {
			window.alert("Enter valid Username");
			$("#userId").val("");
			$("#userId").focus();
			event.preventDefault();
			//		$("#hintLink").attr("href", "login-again.action");
		} else {

			$("#hintLink").attr("href",
					$("#hintLink").attr("href") + "/" + userId);
		}
		console.log("value of userId:" + userId);
	}

	$(document).ready(function() {
		$("#hintLink").click(function(event) {
			hintClick(event);
		});
	});
</script>
</html>