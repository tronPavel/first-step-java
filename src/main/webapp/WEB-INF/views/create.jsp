<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head><title><fmt:message key="posts.create"/></title></head>
<body>
<h2><fmt:message key="posts.create"/></h2>
<form action="/posts" method="post" enctype="multipart/form-data">
  <label for="title"><fmt:message key="posts.title.label"/>:</label>
  <input type="text" id="title" name="title" required>
  <br>
  <label for="content"><fmt:message key="posts.content.label"/>:</label>
  <textarea id="content" name="content" placeholder="<fmt:message key='posts.content.label'/>"></textarea>
  <br>
  <label for="files"><fmt:message key="posts.files.label"/>:</label>
  <input type="file" id="files" name="files" multiple accept="image/*,.pdf">
  <br>
  <button type="submit"><fmt:message key="posts.submit"/></button>
</form>
<a href="/posts"><fmt:message key="posts.back"/></a>
</body>
</html>