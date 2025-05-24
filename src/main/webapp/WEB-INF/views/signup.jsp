<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}" />
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title><fmt:message key="signup.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/layout.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
<form action="signup" method="post">
    <h2><fmt:message key="signup.title"/></h2>
    <label for="email"><fmt:message key="email.label"/>:</label>
    <input type="email" id="email" name="email" placeholder="<fmt:message key='email.label'/>" required>
    <br>
    <label for="login"><fmt:message key="login.placeholder"/>:</label>
    <input type="text" id="login" name="login" placeholder="<fmt:message key='login.placeholder'/>" required>
    <br>
    <label for="password"><fmt:message key="password.placeholder"/>:</label>
    <input type="password" id="password" name="password" placeholder="<fmt:message key='password.placeholder'/>" required>
    <br>
    <button type="submit"><fmt:message key="signup.title"/></button>
    <c:if test="${not empty requestScope.error}">
        <p style="color: red;"><c:out value="${requestScope.error}"/></p>
    </c:if>
    <div class="signup-error"></div> <!-- Контейнер для ошибок валидации -->
</form>
</body>
</html>