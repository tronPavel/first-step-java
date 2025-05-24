package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.service.AuthService;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignUpServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(SignUpServlet.class);
    private AuthService authService = new AuthService();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        renderPage(req, resp, "Регистрация", "/WEB-INF/views/signup.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int userId = authService.register(login, password, email);
                auditLogDAO.logAction(userId, "REGISTER", "User registered with login: " + login, conn);
                conn.commit();
                logger.info("User registered: {}", login);
                resp.sendRedirect("checkemail.jsp");
            } catch (Exception e) {
                conn.rollback();
                logger.error("Error during registration for login: {}", login, e);
                req.setAttribute("error", e.getMessage());
                renderPage(req, resp, "Регистрация", "/WEB-INF/views/signup.jsp");
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}