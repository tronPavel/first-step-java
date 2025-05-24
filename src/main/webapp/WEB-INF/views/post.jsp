<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head><title>${post.title}</title></head>
<body>
<h2>${post.title}</h2>
<p>${post.content}</p>
<h3><fmt:message key="posts.files.label"/>:</h3>
<ul>
    <c:forEach var="file" items="${post.files}">
        <li>
            <a href="/files/${file.id}" target="_blank">${file.fileName}</a>
            (<a href="/files/${file.id}?download=true"><fmt:message key="files.download"/></a>) (${file.contentType})
        </li>
    </c:forEach>
</ul>
<a href="/posts/${post.id}/edit"><fmt:message key="posts.edit"/></a>
<a href="/posts"><fmt:message key="posts.back"/></a>
</body>
</html>