<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 08.03.2025
  Time: 18:24
  To change this template use File | Settings | File Templates.
--%>
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
