<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Профиль</title></head>
<body>
<p>Welcome <%= ((org.example.demo.model.User) request.getAttribute("user")).getLogin() %></p>
<a href="logout">Выйти</a>
</body>
</html>