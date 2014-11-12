<%@ include file='header.jsp'%>

<h2>Quiz Administration - Edit Topic</h2>
<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>
<div class="adminPage">

	<div id="editTopic" class="adminDiv">
		<form action="/Quiz/admin-edit-topic.action" method="POST">
			<div>
				<label><b>Edit Topic</b></label><br>
				<table>
					<tr>
						<th>Topic ID</th>
						<th>Topic Name</th>
					</tr>
					<tr>
						<td>${topic.getTopicId() }</td>
						<td><input type="hidden" name="topicId"
							value="${topic.topicId }"> <input type="text"
							id="editTopic" name="name" size="35" 
							value="${topic.name }"><br></td>
						<td><input type="submit" value="Edit Topic"
							id="editTopicButton"></td>
				</table>
			</div>
		</form>
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