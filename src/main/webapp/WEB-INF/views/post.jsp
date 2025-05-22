<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>${post.title}</title></head>
<body>
<h2>${post.title}</h2>
<p>${post.content}</p>
<h3>Прикреплённые файлы:</h3>
<ul>
    <c:forEach var="file" items="${post.files}">
        <li>
            <a href="/files/${file.id}" target="_blank">${file.fileName}</a>
            (${file.contentType})
        </li>
    </c:forEach>
</ul>
<a href="/posts/${post.id}/edit">Редактировать</a>
<a href="/posts">Назад к списку</a>
</body>
</html>