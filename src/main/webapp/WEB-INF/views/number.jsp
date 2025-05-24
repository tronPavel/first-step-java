<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<form action="number" method="post">
  <input type="number" name="number" placeholder="<fmt:message key='number.title'/>" required>
  <button type="submit"><fmt:message key="posts.submit"/></button>
  <c:if test="${not empty requestScope.error}">
    <p style="color: red;"><c:out value="${requestScope.error}"/></p>
  </c:if>
  <c:if test="${not empty requestScope.result}">
    <p style="color: green;"><fmt:message key="number.title"/>: <c:out value="${requestScope.result}"/></p>
  </c:if>
</form>