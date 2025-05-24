<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head><title><fmt:message key="profile.title"/></title></head>
<body>
<p>Welcome <c:out value="${user.login}"/></p>
<a href="logout"><fmt:message key="profile.logout"/></a>
</body>
</html>