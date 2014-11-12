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
                <td> User Name: </td> <td><input id="userId" type='text' name='userId' size='20' maxlength='20'></td> 
            </tr>
            <tr> 
                <td> Password: </td><td><input type='password' name='password' size='10' maxlength='10'></td>
            </tr>
            <tr> 
                <td> Confirm Password: </td><td><input type='password' id='confirmPassword' name='confirmPpassword' size='10'  maxlength='10'></td>
            </tr>
            <tr> 
                <td> Password Hint: </td><td><input type='text' id="hint" name='hint'  size='20' maxlength='30'></td>
            </tr>
            <tr> 
                <td> Email adddress: </td> <td><input type='text' id="emailAddress" name='emailAddress' size='30' maxlength='40'><br></td> 
            </tr>
        </table>
        <br>
        <input id="newAccountButton" class="centeredButton" type='submit' value='create account'>
        </sf:form>
        
        <br><br>
        <c:import url="footer.jsp"/>
</body>
</html>
<script>
$("#newAccountButton").click(function(event){
	   if ($.trim($("#userId").val()) == "" ){
           alert("Username cannot be empty.");
           event.preventDefault();
           $("#userId").focus();
		   return;
	   }
	   
       if ($.trim($("#password").val()) == "" ){
           alert("Password cannot be empty.");
           event.preventDefault();
           $("#").focus();
           return;
       }
       
	   if ($("#password").val() !== $("#confirmPassword").val()){
	        alert("Passwords must match.");
	        event.preventDefault();
	        $("#").focus();
	        return;
	   }
	   
       if ($.trim($("#hint").val()) == "" ){
           alert("Hint cannot be empty.");
           event.preventDefault();
           $("#password").focus();
           return;
       }
       
       if ($.trim($("#emailAddress").val()) !== "" ){
    	   var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    	   if( !emailReg.test($.trim($("#emailAddress").val())) ) {
               alert("Email Address entered is invalid.");
               event.preventDefault();
               $("#emailAddress").focus();
               return;
    		  }
       }
});
</script>
