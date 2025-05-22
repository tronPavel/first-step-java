<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Редактировать ${post.title}</title></head>
<body>
<h2>Редактировать пост</h2>
<form action="/posts/${post.id}" method="post">
  <input type="text" name="title" value="${post.title}" required>
  <textarea name="content">${post.content}</textarea>
  <button type="submit">Сохранить</button>
</form>
<a href="/posts/${post.id}">Назад к посту</a>
</body>
</html>