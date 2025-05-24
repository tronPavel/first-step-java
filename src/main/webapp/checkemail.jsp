<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}" />
<fmt:setBundle basename="messages" />
<html>
<head><title><fmt:message key="checkemail.title"/></title></head>
<body>
<h2><fmt:message key="checkemail.title"/></h2>
<p><fmt:message key="checkemail.message"/></p>
<p><fmt:message key="checkemail.spam"/></p>
<p><a href="/login"><fmt:message key="checkemail.back"/></a></p>
</body>
</html>