<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Мои посты</title></head>
<body>
<h2>Мои посты</h2>
<a href="/posts/new">Создать новый пост</a>
<ul>
    <c:forEach var="post" items="${posts}">
        <li>
            <a href="/posts/${post.id}">${post.title}</a>
            <a href="/posts/${post.id}/edit">Редактировать</a>
            <form action="/posts/${post.id}/delete" method="post" style="display:inline;">
                <button type="submit" onclick="return confirm('Вы уверены, что хотите удалить этот пост?');">Удалить</button>
            </form>
        </li>
    </c:forEach>
</ul>
</body>
</html>