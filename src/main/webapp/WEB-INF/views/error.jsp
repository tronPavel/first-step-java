<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Ошибка</title></head>
<body>
<h1>Произошла ошибка</h1>
<p>Код: <%= request.getAttribute("jakarta.servlet.error.status_code") %></p>
<p>Сообщение: <%= request.getAttribute("jakarta.servlet.error.message") %></p>
<a href="/login">Вернуться</a>
</body>
</html>