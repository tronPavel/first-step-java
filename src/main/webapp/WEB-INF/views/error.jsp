<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}" />
<fmt:setBundle basename="messages" />
<html>
<head><title><fmt:message key="error.title"/></title></head>
<body>
<h1><fmt:message key="error.title"/></h1>
<p><fmt:message key="error.server"/> <%= request.getAttribute("jakarta.servlet.error.status_code") %></p>
<a href="/login"><fmt:message key="checkemail.back"/></a>
</body>
</html>