<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Произошла ошибка</title>
</head>
<body>
<h1>Упс! Произошла ошибка</h1>
<p>Код ошибки: <%= request.getAttribute("jakarta.servlet.error.status_code") %></p>
</body>
</html>
