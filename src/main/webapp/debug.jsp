<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
 <br><br>
 <p>DEBUG INFO<br><br></p>

    request.getRemoteAddr() = <%= request.getRemoteAddr() %>
 
      <br><br><br>
      Request Parameters:
      <table border=1 >
      <tr><th>Parameter Name</th><th>Parameter Value</th></tr>
      <c:forEach var="item" items="${param}">
        <tr>
          <td><c:out value="${item.key}"/></td>
          <td><c:out value="${item.value}"/></td>
        </tr>
      </c:forEach>
      </table>


      <br><br><br>
      Request Attributes:
      <table border=1 >
      <tr><th>Attribute Name</th><th>Attribute Value</th></tr>
      <c:forEach var="item" items="${requestScope}">
        <tr>
          <td><c:out value="${item.key}"/></td>
          <td><c:out value="${item.value}"/></td>
        </tr>
      </c:forEach>
      </table>


      <br><br><br>
      Request Headers:
      <table border=1 >
       <c:forEach var='hdr' items='${header}'>
             <tr>
               <td><c:out value='${hdr.key}'/></td>
               <td><c:out value="${hdr.value}"/></td>
             </tr>
       </c:forEach>
       </table>

      

      <br><br><br>
      Cookies:
      <table border=1 >
      <tr><th>Cookie Name</th><th>Cookie Value</th></tr>
      <c:forEach var="item" items="${cookie}">
        <tr>
          <td><c:out value="${item.key}"/></td>
          <td><c:out value="${item.value.value}"/></td>
        </tr>
      </c:forEach>
      </table>
      
      <br><br><br>
      Session Attributes:
      <table border=1 >
      <tr><th>Attribute Name</th><th>Attribute Value</th></tr>
      <c:forEach var="item" items="${sessionScope}">
        <tr>
          <td><c:out value="${item.key}"/></td>
          <td><c:out value="${item.value}"/></td>
        </tr>
      </c:forEach>
      </table>
      
      
      <br><br><br>
      Application Attributes:
      <table border=1 >
      <tr><th>Attribute Name</th><th>Attribute Value</th></tr>
      <c:forEach var="item" items="${applicationScope}">
        <tr>
          <td><c:out value="${item.key}"/></td>
          <td><c:out value="${item.value}"/></td>
        </tr>
      </c:forEach>
      </table>

