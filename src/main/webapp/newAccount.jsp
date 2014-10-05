<%@ include file='header.jsp' %>
        <h2>Open a New Account</h2>
		<br>
		<c:if test="${not empty reqErrorMessage}">
		<p class="errortext">Error: ${reqErrorMessage}</p>
		</c:if>
        <hr>
        
        <sf:form action="new-account.action" method="post">
        
        <table class="newaccount" >
            <tr> 
                <td> User Name: </td> <td><input type='text' name='userId' size='20' maxsize='20'></td> 
            </tr>
            <tr> 
                <td> Password: </td><td><input type='password' name='password' size='10' maxsize='10'></td>
            </tr>
            <tr> 
                <td> Confirm Password: </td><td><input type='password' name='confirmPpassword' size='10'  maxsize='10'></td>
            </tr>
            <tr> 
                <td> Password Hint: </td><td><input type='text' name='hint'  size='20' maxsize='30'></td>
            </tr>
            <tr> 
                <td> Email adddress: </td> <td><input type='text' name='email' size='30' maxsize='40'><br></td> 
            </tr>
        </table>
        <br>
        <input class="centeredButton" type='submit' value='create account'>
        </sf:form>
        
        <br><br>
        <c:import url="footer.jsp"/>
</body>
</html>
