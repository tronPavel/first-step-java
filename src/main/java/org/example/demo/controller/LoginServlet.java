package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.model.User;
import org.example.demo.service.AuthService;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(LoginServlet.class);
    private AuthService authService = new AuthService();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

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

        try (Connection conn = DBConnection.getConnection()) {
            User user = authService.authenticate(login, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("locale", "ru"); // Default locale

                Cookie localeCookie = new Cookie("locale", "ru");
                localeCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                response.addCookie(localeCookie);

                auditLogDAO.logAction(user.getId(), "LOGIN", "User logged in: " + login, conn);
                logger.info("User logged in: {}, userId: {}", login, user.getId());

                response.sendRedirect("profile");
            } else {
                auditLogDAO.logAction(0, "LOGIN_FAILED", "Failed login attempt for: " + login, conn);
                logger.warn("Invalid login or password for user: {}", login);
                request.setAttribute("error", "Неверный логин или пароль");
                renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
            }
        } catch (SQLException e) {
            logger.error("Database error during login for user: {}", login, e);
            request.setAttribute("error", "Ошибка сервера. Попробуйте позже.");
            renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
        }
    }
}