<%@ include file='header.jsp'%>

<h2>Quiz Administration - Update User</h2>
<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>
<div class="adminPage">

	<div id="editUser" class="adminDiv">
		<form action="/Quiz/edit-user-u.action" method="POST">
			<div>
				<label><b>Edit User</b></label><br>
				<table>
					<tr>
						<td><label>Username</label><br> 
						<input type="text" name="disabledUserId"
							value="${sessionScope.user.userId }" disabled>
							<input type="hidden" name="userId" value="${sessionScope.user.userId }"></td>
					</tr>
					<tr>
						<td><label>Password</label><br>
						<input type="password" id="editPassword" name="password"
							size="10" maxlength="10" value="${sessionScope.user.password }"><br>
						</td>
					</tr>
					<tr>
						<td><label>Confirm Password</label><br>
                            <input type="password" id="confirmPassword"
							name="confirmPassword" size="10" maxlength="10"
							value="${sessionScope.user.password }"><br></td>
					</tr>
					<tr>
						<td><label>Password Hint</label><br> <input type="text"
							id="editHint" name="hint" size="20" maxlength="30"
							value="${sessionScope.user.hint }"><br></td>
					</tr>
					<tr>
						<td><label>Email Address</label><br> <input type="text"
							id="editEmailAddress" name="emailAddress" size="30" maxlength="40"
							value="${sessionScope.user.emailAddress }"><br></td>
					</tr>
					<tr>
						<td><input type="submit" value="Edit User"
							id="editUserButton"></td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</div>
<br>
<br>
<c:import url="footer.jsp" />
</body>
</html>