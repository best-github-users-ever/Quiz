
<%@ include file='header.jsp'%>
<h2>Take Quiz</h2>

<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
	<br>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>

<form action='<%=response.encodeURL("answerQuestion-u.action")%>' method='POST'>

	<table border="0">

		<tr>
			<td> <b>Question:</b><br>
			<s:property value="#session.question.question"/></td>
		</tr>
		<br>
		
		<tr>
		<td><b>Answer</b> <br> 
		<input type="radio" name="option" value="0"><s:property value="#session.question.option1"/><br>
			<input type="radio" name="option" value="1"><s:property value="#session.question.option2"/><br>
			<input type="radio" name="option" value="2"><s:property value="#session.question.option3"/><br>
			<input type="radio" name="option" value="3"><s:property value="#session.question.option4"/><br></td>
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