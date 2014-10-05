
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

	<table class="questionTable">

		<tr>
			<td> <b>Question:</b><br>
			${sessionScope.question.question}</td>
		</tr>
		<br>
		
		<tr>
		<td><b>Answer</b> <br> 
		<input type="radio" name="option" value="0">${sessionScope.question.option1}<br>
			<input type="radio" name="option" value="1">${sessionScope.question.option2}<br>
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
<br>
        <h1>WebSocket test</h1>
        <script>
        var ws = new WebSocket("ws://127.0.0.1:8080/");

        ws.onopen = function() {
            alert("Opened!");
            ws.send("Hello Server");
        };

        ws.onmessage = function (evt) {
            alert("Message: " + evt.data);
        };

        ws.onclose = function() {
            alert("Closed!");
        };

        ws.onerror = function(err) {
            alert("Error: " + err);
        };
        </script>
<c:import url="footer.jsp" />
</body>
</html>