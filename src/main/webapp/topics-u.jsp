
<%@ include file='header.jsp'%>
<h2>Select Topic</h2>

<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
	<br>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>

<form action='<%=response.encodeURL("chooseQuizTopic-u.action")%>'
	method='POST'>

	<table class="topicsTable">

		<tr>
			<td>Topic:</td>
			<td><select name="topicId">
					<c:forEach var="topic" items="${topicList}">
						<option value="${topic.topicId}">${topic.name}
					</c:forEach>
			</select></td>
		</tr>

		<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td>Number of Players</td>
			<td><select name="numberPlayers">
					<option value="1">1
					<option value="2">2
					<option value="3">3
					<option value="4">4
					<option value="5">5
			</select></td>
		</tr>

		<tr>
			<td>&nbsp</td>
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