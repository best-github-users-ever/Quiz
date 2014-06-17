
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

<h1>jQuiz Demo Page</h1>
<div class="questionContainer radius">
    <div class="question"><b>Question 1:</b> Inside which HTML element do we put the JavaScript?</div>
    <div class="answers">
        <ul>
            <li>
                <label><input type="radio" name="q1" id="q1-a" />&lt;js&gt;</label>
            </li>
            <li>
                <label><input type="radio" name="q1" id="q1-b" />&lt;script&gt;</label>
            </li>
            <li>
                <label><input type="radio" name="q1" id="q1-c" />&lt;scripting&gt;</label>
            </li>
            <li>
                <label><input type="radio" name="q1" id="q1-d" />&lt;javascript&gt;</label>
            </li>
        </ul>
    </div>
    <div class="btnContainer">
        <div class="prev"></div>
        <div class="next">
            <a class="btnNext">Next &gt;&gt;</a>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="questionContainer hide radius">
    <div class="question"><b>Question 2:</b> What is the correct JavaScript syntax to write "Hello World"?</div>
    <div class="answers">
        <ul>
            <li>
                <label><input type="radio" name="q2" id="q2-a" />response.write("Hello World")</label>
            </li>
            <li>
                <label><input type="radio" name="q2" id="q2-b" />("Hello World")</label>
            </li>
            <li>
                <label><input type="radio" name="q2" id="q2-c" />"Hello World"</label>
            </li>
            <li>
                <label><input type="radio" name="q2" id="q2-d" />document.write("Hello World")</label>
            </li>
        </ul>
    </div>
    <div class="btnContainer">
        <div class="prev">
            <a class="btnPrev">&lt;&lt; Prev</a>
        </div>
        <div class="next">
            <a class="btnNext">Next &gt;&gt;</a>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="questionContainer radius hide">
    <div class="question"><b>Question 3:</b> Where is the correct place to insert a JavaScript?</div>
    <div class="answers">
        <ul>
            <li>
                <label><input type="radio" name="q3" id="q3-a" />Both the &lt;thead&gt; section and the &lt;body&gt; section are correct</label>
            </li>
            <li>
                <label><input type="radio" name="q3" id="q3-b" />The &lt;body&gt; section</label>
            </li>
            <li>
                <label><input type="radio" name="q3" id="q3-c" />The &lt;head&gt; section</label>
            </li>
        </ul>
    </div>
    <div class="btnContainer">
        <div class="prev">
            <a class="btnPrev">&lt;&lt; Prev</a>
        </div>
        <div class="next">
            <a class="btnNext">Next &gt;&gt;</a>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="questionContainer radius hide">
    <div class="question"><b>Question 4:</b> What is the correct syntax for referring to an external script called "xxx.js"?</div>
    <div class="answers">
        <ul>
            <li>
                <label><input type="radio" name="q4" id="q4-a" />&lt;script type="text/javascript" name="xxx.js"&gt;</label>
            </li>
            <li>
                <label><input type="radio" name="q4" id="q4-b" />&lt;script type="text/javascript" href="xxx.js"&gt;</label>
            </li>
            <li>
                <label><input type="radio" name="q4" id="q4-c" />&lt;script type="text/javascript" src="xxx.js"&gt;</label>
            </li>
        </ul>
    </div>
    <div class="btnContainer">
        <div class="prev">
            <a class="btnPrev">&lt;&lt; Prev</a>
        </div>
        <div class="next">
            <a class="btnNext">Next &gt;&gt;</a>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="questionContainer radius hide">
    <div class="question"><b>Question 5:</b> The external JavaScript file must contain the &lt;script&gt; tag?</div>
    <div class="answers">
        <ul>
            <li>
                <label><input type="radio" name="q5" id="q5-a" />True</label>
            </li>
            <li>
                <label><input type="radio" name="q5" id="q5-b" />False</label>
            </li>
        </ul>
    </div>
    <div class="btnContainer">
        <div class="prev">
            <a class="btnPrev">&lt;&lt; Prev</a>
        </div>
        <div class="next">
            <a class="btnShowResult">Finish!</a>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="txtStatusBar">Status Bar</div>
<div id="progressKeeper" class="radius">
    <div id="progress"></div>
</div>
<div id="resultKeeper" class="radius hide"></div>

	<br>
	<br>
	<p>Test to see if javascript is working in the browswer: <b id='boldStuff'>test</b> </p> 
	<input type='button' onclick='changeText()' value='Change Text'/>
	<br>
	<br>


<script src="quiz.js">
runQuiz();
</script>

<c:import url="footer.jsp" />
</body>
</html>