package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.dao.UserDAO;
import org.example.demo.model.User;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(ProfileServlet.class);
    private UserDAO userDAO = new UserDAO();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            logger.warn("Unauthorized access to profile, redirecting to login");
            response.sendRedirect("login");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                logger.warn("User not found for userId: {}", userId);
                request.getSession().invalidate();
                response.sendRedirect("login?error=user_not_found");
                return;
            }

            auditLogDAO.logAction(userId, "VIEW_PROFILE", "Viewed profile", conn);
            logger.info("Profile fetched for userId: {}", userId);
            request.setAttribute("user", user);
            renderPage(request, response, "Профиль", "/WEB-INF/views/profile.jsp");
        } catch (SQLException e) {
            logger.error("Database error while fetching profile for userId: {}", userId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}