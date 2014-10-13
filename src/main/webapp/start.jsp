<%@ include file='header.jsp'%>
<h2>Test Spring 4 WebSocket interface w/ Add</h2>
<noscript>
	<h2>Enable Java script and reload this page to run Websocket Demo</h2>
</noscript>
<div class="websocketDiv">
	<div>
		<div>
			<button id="connect" onclick="connect();">Connect</button>
			<button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
			<br />
			<br />
		</div>
		<div id="calculationDiv">
			<label>Number One:</label><input type="text" id="num1" /><br /> <label>Number
				Two:</label><input type="text" id="num2" /><br />
			<br />
			<button id="sendNum" onclick="sendNum();">Send to Add</button>
			<p id="calResponse"></p>
		</div>
	</div>
</div>

<div class="login">
	<c:url var="loginURL" value="/login-again.action">
	</c:url>
	Click <a href="${loginURL}"> here</a> to go to login page.
</div>


</body>
</html>
