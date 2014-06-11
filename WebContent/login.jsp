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

<s:form action="login.action" method="post">
	<table class="loginTable">
		<tr>
			<td class='userinput' ><b>Username:</b></td>
			<td class='userinput' ><input type='text' name='userId' value=''></td>
		</tr>
		<tr>
			<td><b>Password:</b></td>
			<td><input type='password' name='password' size='8'></td>
		</tr>
		<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td>&nbsp</td>
			<td>&nbsp</td>
		</tr>
		<tr>
			<td colspan="2"><input class="centeredButton" type='submit'
				value='login'></td>
		</tr>
		<tr>
            <s:url var="newAccountURL" action="request-new-account.action" >  </s:url>  
			<td colspan="2">Click <a
					href="<s:property value="#newAccountURL"/>">
					here</a> to open a new account.
			</td>
		</tr>

		<c:if test="${not empty reqErrorMessage}">
			<tr>
				<td colspan="2">
				
                <s:url var="hintURL" action="show-hint.action" >  </s:url>  
				Click <a
					href="<s:property value="#hintURL"/>"> here</a> to
					see your password hint.
				</td>
			</tr>
		</c:if>
	</table>
	</s:form>
	<br>

	
<br>
<br>
<c:import url="footer.jsp" />
</body>
</html>
