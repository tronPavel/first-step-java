<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale != null ? sessionScope.locale : 'ru'}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title><fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/layout.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
<header>
    <p class="header__logo"><fmt:message key="app.title"/></p>
    <nav class="header__menu">
        <ul class="header__menu-list">
            <c:choose>
                <c:when test="${sessionScope.userId == null}">
                    <li class="header__menu-item"><a class="header__menu-link" href="login"><fmt:message key="login.title"/></a></li>
                    <li class="header__menu-item"><a class="header__menu-link" href="signup"><fmt:message key="signup.title"/></a></li>
                </c:when>
                <c:otherwise>
                    <li class="header__menu-item"><a class="header__menu-link" href="profile"><fmt:message key="profile.title"/></a></li>
                    <li class="header__menu-item"><a class="header__menu-link" href="posts"><fmt:message key="posts.title"/></a></li>
                    <li class="header__menu-item"><a class="header__menu-link" href="logout"><fmt:message key="profile.logout"/></a></li>
                </c:otherwise>
            </c:choose>
            <li class="header__menu-item"><a class="header__menu-link" href="number"><fmt:message key="number.title"/></a></li>
        </ul>
        <div class="locale-switcher">
            <a href="${pageContext.request.contextPath}/locale?lang=ru">RU</a> | <a href="${pageContext.request.contextPath}/locale?lang=en">EN</a>
        </div>
    </nav>
</header>
<main>
    <jsp:include page="${contentPage}"/>
</main>
<footer>
    <p class="footer__text"><fmt:message key="footer.text"/></p>
</footer>
</body>
</html>