<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<html>
<head>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="../../styles/layout.css">
    <script src="../../js/main.js"></script>
</head>
<body>
    <header>
        <p class="header__logo">
            WEB BASE/Servlets
        </p>

        <nav class="header__menu">
            <ul class="header__menu-list">
                <%
                    Object user = session.getAttribute("user");
                    if (user == null) {
                %>
                <li class="header__menu-item"> <a class="header__menu-link" href="login">log in</a></li>
                <li class="header__menu-item"> <a class="header__menu-link" href="signup">sign up</a></li>
                <%
                } else {
                %>
                <li class="header__menu-item"> <a class="header__menu-link" href="profile">Профиль</a></li>
                <li class="header__menu-item"> <a class="header__menu-link" href="logout">Выйти</a></li>
                <%
                    }
                %>
                <li class="header__menu-item"> <a class="header__menu-link" href="number">Передача числа</a> </li>

            </ul>

        </nav>

    </header>
    <main>
    <jsp:include page="${contentPage}" />
    </main>
    <footer>
        <p class="footer__text">
            2025 <br>
            футер для галочки
        </p>
    </footer>
</body>
</html>
