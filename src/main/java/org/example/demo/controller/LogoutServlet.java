package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LogoutServlet.class);
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        if (session != null) {
            try (Connection conn = DBConnection.getConnection()) {
                if (userId != null) {
                    auditLogDAO.logAction(userId, "LOGOUT", "User logged out", conn);
                    logger.info("User logged out, userId: {}", userId);
                }
                session.invalidate();
            } catch (SQLException e) {
                logger.error("Error logging logout action for userId: {}", userId, e);
            }
        }
        response.sendRedirect("login");
    }
}