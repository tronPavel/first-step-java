package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.model.User;
import org.example.demo.service.AuthService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try {
            User user = authService.authenticate(login, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                response.sendRedirect("profile");
            } else {
                request.setAttribute("error", "Неверный логин или пароль");
                renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Ошибка сервера. Попробуйте позже.");
            renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
        }
    }
}