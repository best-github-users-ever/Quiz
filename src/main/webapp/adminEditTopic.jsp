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

	<div id="editTopicDiv" class="adminDiv">
		<form action="/Quiz/admin-edit-topic.action" method="POST">
			<div>
				<label><b>Edit Topic</b></label><br>
				<table>
					<tr>
						<th>Topic ID</th>
						<th>Topic Name</th>
					</tr>
					<tr>
						<td>${topic.getTopicId() }<input type="hidden" name="topicId"
							value="${topic.topicId }">
						</td>
						<td><input type="text" id="editTopic" name="name" size="35"
							value="${topic.name }"></td>
					<tr>
						<td colspan="2"><input type="submit" value="Edit Topic"
							id="editTopicButton"></td>
					</tr>
				</table>
			</div>
		</form>
	</div>

</div>

<br>
<c:import url="footer.jsp" />
</body>
</html>
<script>
	var submitClick = function(event) {
		var topic = $("#editTopic").val();

		if ($.trim(topic) === "") {
			window.alert("Enter valid Topic Name");
			topic.focus();
			event.preventDefault();
			return;
		}
	}

	$(document).ready(function() {
		$("#editTopicButton").click(function(event) {
			submitClick(event);
		});
	});
</script>
