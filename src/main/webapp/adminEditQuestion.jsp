<%@ include file='header.jsp'%>

<h2>Quiz Administration - Edit Question</h2>
<br>
<c:if test="${not empty reqErrorMessage}">
	<p class="errortext">Error: ${reqErrorMessage}</p>
</c:if>
<c:if test="${not empty reqPositiveMessage}">
	<p class="positivemessage">${reqPositiveMessage}</p>
</c:if>
<div class="adminPage">

	<div id="editQuestion" class="adminDiv">
		<form action="/Quiz/admin-edit-question.action" method="POST">
			<div>
			   <label><b>Edit Question</b></label><br>
			   <table>
				   <tr>
				   <td> <label>The Question</label><br> 
                       <input type="hidden" name="questionId" value="${question.questionId }">
                       <input type="text"
                        id="editQuestion" name="question" size="80"  value="${question.question }"><br> 
				   </td>
				   <tr>
				   
				   <td>
                 <label>Choose Topic for this Question</label><br> 
                 <select name="topicId">
                   <c:forEach var="topic" items="${topicList}">
                       <option value="${topic.topicId}"
                       <c:if test="${question.topicId == topic.topicId }">
                     selected 
                      	</c:if>
                     >${topic.name} 
                   </c:forEach>
                 </select>
             	  </td>
             	  </tr>
             	  <tr>
                    <td> <label>Option 1</label><br> 
                       <input type="text"
                        id="editOption1" name="option1" size="40"  value="${question.option1 }"><br> 
                    </td>
                    </tr>
                    <tr>
                    <td> <label>Option 2</label><br> 
                       <input type="text"
                        id="editOption2" name="option2" size="40"  value="${question.option2 }"><br> 
                    </td>
                    </tr>
                    <tr>
                    <td> <label>Option 3</label><br> 
                       <input type="text"
                        id="editOption3" name="option3" size="40"  value="${question.option3 }"><br> 
                    </td>
                    </tr>
                    <tr>
                    <td> <label>Option 4</label><br> 
                       <input type="text"
                        id="editOption4" name="option4" size="40"  value="${question.option4 }"><br> 
                    </td>
                    </tr>
                    <tr>
                    <td> 
                      <label>Answer is 1, 2, 3 or 4</label><br> 
                      <input type="radio" name="answerIdx" value="0" checked="${question.answerIdx == 0 }">1<br> 
                      <input type="radio" name="answerIdx" value="1" checked="${question.answerIdx == 1 }">2<br> 
                      <input type="radio" name="answerIdx" value="2" checked="${question.answerIdx == 2 }">3<br> 
                      <input type="radio" name="answerIdx" value="3" checked="${question.answerIdx == 3 }">4<br> 
                    </td>
                    </tr>
                    <tr>
					<td>
					   <input type="submit" value="Edit Question" id="editQuestionButton">
					</td>
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