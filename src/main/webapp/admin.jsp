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
		<form action="/Quiz/admin-new-topic.action" method="POST">
			<div>
				<label><b>Add Topic</b></label><br> <input type="text"
					id="newTopic" name="name" size="30"><br> <input
					type="submit" value="Add Topic" id="addTopicButton">
			</div>
		</form>
	</div>

	<div id="newQuestion" class="adminDiv">
		<br> <br>
		<form action="/Quiz/admin-new-question.action" method="POST">

			<label><b>Add Question</b></label><br> <br> <label>Choose
				Topic for this Question</label><br> <select name="topicId">
				<c:forEach var="topic" items="${topicList}">
					<option value="${topic.topicId}">${topic.name}
				</c:forEach>
			</select> <br> <br> <label>The Question</label><br> <input
				name="question" type="text" id="newQuestion" size="80"><br>
			<br> <label>Option 1</label><br> <input type="text"
				id="newOption1" size="80" name="option1"><br> <label>Option
				2</label><br> <input type="text" id="newOption2" size="80"
				name="option2"><br> <label>Option 3</label><br> <input
				type="text" id="newOption3" size="80" name="option3"><br>
			<label>Option 4</label><br> <input type="text" id="newOption4"
				size="80" name="option4"><br> <br> <label>Answer
				is 1, 2, 3 or 4</label><br> <input type="radio" name="answerIdx"
				value="0">1<br> <input type="radio" name="answerIdx"
				value="1">2<br> <input type="radio" name="answerIdx"
				value="2">3<br> <input type="radio" name="answerIdx"
				value="3">4<br> <input type="submit"
				value="Add Question" id="addQuestionButton">
	   </form>
    </div>
</div>
<br>
<div id="editDeleteTopic" class="adminDiv">
	<div>
		<label><b>Topics</b></label><br>
		<table>
			<tr>
				<th></th>
				<th></th>
				<th>TopicId</th>
				<th>Topic Name</th>
			</tr>

			<c:forEach var="topic" items="${topicList}">
				<tr>
					<td><a
						href="<spring:url value='/admin-delete-topic.action/{topicId}'>
								<spring:param name='topicId' value='${topic.topicId}' />
							</spring:url>">delete</a></td>
					<td><a
						href="<spring:url value='/admin-edit-topic-req.action/{topicId}'>
								<spring:param name='topicId' value='${topic.topicId}' />
							</spring:url>">edit</a></td>
					<td>${topic.topicId}</td>
					<td>${topic.name}</td>
				</tr>
			</c:forEach>

		</table>
	</div>
</div>
<div id="editDeleteQuestion" class="adminDiv">
	<div>
		<label><b>Questions</b></label><br>
		<table>
			<tr>
				<th></th>
				<th></th>
				<th>Question Id</th>
				<th>Topic Id</th>
				<th>Question</th>
				<th>Option 1</th>
				<th>Option 2</th>
				<th>Option 3</th>
				<th>Option 4</th>
				<th>Answer Idx</th>
			</tr>

			<c:forEach var="question" items="${adminQuestionsList}">
				<tr>
					<td><a
						href="<spring:url value='/admin-delete-question.action/{questionId}'>
								<spring:param name='questionId' value='${question.questionId}' />
							</spring:url>">delete</a></td>
					<td><a
						href="<spring:url value='/admin-edit-question-req.action/{questionId}'>
								<spring:param name='questionId' value='${question.questionId}' />
							</spring:url>">edit</a></td>
					<td>${question.questionId}</td>
					<td>${question.topicId}</td>
					<td>${question.question}</td>
					<td>${question.option1}</td>
					<td>${question.option2}</td>
					<td>${question.option3}</td>
					<td>${question.option4}</td>
					<td>${question.answerIdx}</td>
				</tr>
			</c:forEach>

		</table>
	</div>
</div>

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