<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head><title><fmt:message key="posts.list"/></title></head>
<body>
<h2><fmt:message key="posts.list"/></h2>
<a href="/posts/new"><fmt:message key="posts.create"/></a>
<ul>
    <c:forEach var="post" items="${posts}">
        <li>
            <a href="/posts/${post.id}">${post.title}</a>
            <a href="/posts/${post.id}/edit"><fmt:message key="posts.edit"/></a>
            <form action="/posts/${post.id}/delete" method="post" style="display:inline;">
                <button type="submit" onclick="return confirm('<fmt:message key="posts.delete.confirm"/>');"><fmt:message key="posts.delete"/></button>
            </form>
        </li>
    </c:forEach>
</ul>
</body>
</html>