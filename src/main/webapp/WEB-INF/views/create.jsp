<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Создать пост</title></head>
<body>
<h2>Создать новый пост</h2>
<form action="/posts" method="post" enctype="multipart/form-data">
  <label for="title">Заголовок:</label>
  <input type="text" id="title" name="title" required>
  <br>
  <label for="content">Текст:</label>
  <textarea id="content" name="content" placeholder="Текст поста"></textarea>
  <br>
  <label for="files">Прикрепить файлы:</label>
  <input type="file" id="files" name="files" multiple accept="image/*,.pdf">
  <br>
  <button type="submit">Создать</button>
</form>
<a href="/posts">Назад к списку</a>
</body>
</html>