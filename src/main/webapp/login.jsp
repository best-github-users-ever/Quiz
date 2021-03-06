<%@ include file='header.jsp'%>

<h2>Please Login</h2>
<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>
<hr>

<form action="/Quiz/login.action" method="post">
	<table class="loginTable">
		<tr>
			<td class='userinput'><b>Username:</b></td>
			<td class='userinput'><input type='text' id="userId"
				name='userId' value="${userName }"></td>
		</tr>
		<tr>
			<td><b>Password:</b></td>
			<td><input type='password' id="password" name='password' size='8'></td>
		</tr>
 		<tr>
			<td><pre> </pre></td>
			<td><pre> </pre></td>
		</tr>
		<tr>
			<td colspan="2"><input id="loginButton" class="centeredButton" type='submit'
				value='login'></td>
		</tr>
		<tr>
			<c:url var="newAccountURL" value="/request-new-account.action">
			</c:url>
			<td colspan="2">Click <a href="${newAccountURL}"> here</a> to
				open a new account.
			</td>
		</tr>

		<c:if test="${not empty reqErrorMessage}">
			<tr>
				<td colspan="2"><c:url var="hintURL" value="/show-hint.action">
					</c:url> Click <a id="hintLink" href="${hintURL}"> here</a> to see your
					password hint.</td>
			</tr>
		</c:if>
	</table>
</form>
<br>


<br>
<br>
<c:import url="footer.jsp" />
</body>
<script>
var submitClick = function(event){
	   if ($.trim($("#userId").val()) == "" ){
        alert("Username cannot be empty.");
        event.preventDefault();
        $("#userId").focus();
		   return;
	   }
	   
    if ($.trim($("#password").val()) == "" ){
        alert("Password cannot be empty.");
        event.preventDefault();
        $("#password").focus();
        return;
    }
}
 	function hintClick(event) {
		var userId = $.trim($("#userId").val());

		if (!userId.match(/^[0-9a-z]+$/)) {
			window.alert("Enter valid Username");
			$("#userId").val("");
			$("#userId").focus();
			event.preventDefault();
			//		$("#hintLink").attr("href", "login-again.action");
		} else {

 			$("#hintLink").attr("href",
 					$("#hintLink").attr("href") + "?userId=" + userId);
		}
 		console.log("value of userId:" + userId);
	}

	$(document).ready(function() {
		$("#hintLink").click(function(event) {
			hintClick(event);
		});
		$("#loginButton").click(function(event) {
			submitClick(event);
		});
	});
</script>
</html>
