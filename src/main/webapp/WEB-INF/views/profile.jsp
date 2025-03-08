<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.demo.models.User" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect("login.jsp");
    return;
  }
%>
<html>
<head>
  <title>Профиль</title>
</head>
<body>
<p>дарова <%= user.getLogin() %></p>
<a href="logout">Выйти</a>
</body>
</html>
