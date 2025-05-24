<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head><title><fmt:message key="posts.edit"/> ${post.title}</title></head>
<body>
<h2><fmt:message key="posts.edit"/></h2>
<form action="/posts/${post.id}" method="post">
  <input type="text" name="title" value="${post.title}" required>
  <textarea name="content">${post.content}</textarea>
  <button type="submit"><fmt:message key="posts.submit"/></button>
</form>
<a href="/posts/${post.id}"><fmt:message key="posts.back"/></a>
</body>
</html>