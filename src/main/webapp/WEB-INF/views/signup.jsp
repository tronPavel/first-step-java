<%@ page contentType="text/html;charset=UTF-8" language="java" %>

        <form action="signup" method="post">
            <input type="email" name="email" placeholder="email" required>
            <input type="text" name="login" placeholder="логин" required>
            <input type="password" name="password" placeholder="пароль" required>
            <button type="submit">зарегистрироваться</button>
            <% if (request.getAttribute("error") != null) { %>
            <p style="color: red;"><%= request.getAttribute("error") %></p>
            <% } %>
        </form>


