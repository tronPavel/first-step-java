package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.model.Post;
import org.example.demo.model.User;
import org.example.demo.service.DBConnection;
import org.example.demo.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/posts/*")
public class PostServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(PostServlet.class);
    private PostService postService = new PostService();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            logger.warn("Invalid post ID in request: {}", pathInfo);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID поста");
            return;
        }

        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser == null) {
            logger.warn("No authenticated user found, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String[] parts = pathInfo.split("/");
        int id;
        try {
            id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            logger.error("Invalid post ID format: {}", parts[1], e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID поста");
            return;
        }

        try {
            Post post = postService.getPostById(id);
            if (post == null || post.getUserId() != currentUser.getId()) {
                logger.warn("Access denied for post ID: {}, user ID: {}", id, currentUser.getId());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
                return;
            }

            auditLogDAO.logAction(currentUser.getId(), "VIEW_POST", "Viewed post ID: " + id);

            request.setAttribute("post", post);
            if (parts.length > 2 && "edit".equals(parts[2])) {
                logger.debug("Rendering edit page for post ID: {}", id);
                renderPage(request, response, "Редактировать пост", "/WEB-INF/views/edit.jsp");
            } else {
                logger.debug("Rendering post page for ID: {}", id);
                renderPage(request, response, "Пост", "/WEB-INF/views/post.jsp");
            }
        } catch (SQLException e) {
            logger.error("Database error while fetching post ID: {}", id, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            logger.warn("Invalid post ID in request: {}", pathInfo);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID поста");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            logger.warn("Unauthorized access to post modification");
            response.sendRedirect("/login");
            return;
        }
        Integer userId = (Integer) session.getAttribute("userId");

        String[] parts = pathInfo.split("/");
        int id;
        try {
            id = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            logger.error("Invalid post ID format: {}", parts[1], e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID поста");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Post post = postService.getPostById(id);
                if (post == null || post.getUserId() != userId) {
                    logger.warn("Access denied for post ID: {}, user ID: {}", id, userId);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
                    return;
                }

                if (parts.length > 2 && "delete".equals(parts[2])) {
                    postService.deletePost(id);
                    auditLogDAO.logAction(userId, "DELETE_POST", "Deleted post ID: " + id, conn);
                    logger.info("Post deleted: ID={}, userId: {}", id, userId);
                    conn.commit();
                    response.sendRedirect("/posts");
                } else {
                    post.setTitle(request.getParameter("title"));
                    post.setContent(request.getParameter("content"));
                    postService.updatePost(post);
                    auditLogDAO.logAction(userId, "UPDATE_POST", "Updated post ID: " + id, conn);
                    logger.info("Post updated: ID={}, userId: {}", id, userId);
                    conn.commit();
                    response.sendRedirect("/posts/" + id);
                }
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Database error while processing post ID: {}", id, e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
                throw e;
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}