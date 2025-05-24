<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}" />
<fmt:setBundle basename="messages"  />
<html>
<head><title><fmt:message key="notfound.title"/></title></head>
<body>
<h1><fmt:message key="notfound.title"/></h1>
<a href="/login"><fmt:message key="checkemail.back"/></a>
</body>
</html>