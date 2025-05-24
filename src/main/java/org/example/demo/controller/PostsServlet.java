package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.demo.dao.AuditLogDAO;
import org.example.demo.model.File;
import org.example.demo.model.Post;
import org.example.demo.service.DBConnection;
import org.example.demo.service.PostService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({"/posts", "/posts/new"})
@MultipartConfig(
        maxFileSize = 16 * 1024 * 1024,
        maxRequestSize = 64 * 1024 * 1024
)
public class PostsServlet extends BaseServlet {
    private static final Logger logger = LogManager.getLogger(PostsServlet.class);
    private PostService postService = new PostService();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            logger.warn("Unauthorized access to posts page, redirecting to login");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        if ("/posts/new".equals(path)) {
            renderPage(req, res, "Создать пост", "/WEB-INF/views/create.jsp");
        } else {
            try {
                List<Post> posts = postService.getPostsByUserId(userId);
                req.setAttribute("posts", posts);
                auditLogDAO.logAction(userId, "VIEW_POSTS", "Viewed list of posts");
                logger.info("Posts fetched for userId: {}", userId);
                renderPage(req, res, "Посты", "/WEB-INF/views/posts.jsp");
            } catch (SQLException e) {
                logger.error("Database error while fetching posts for userId: {}", userId, e);
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            logger.warn("Unauthorized access to post creation");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(userId);

        List<File> files = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                List<Part> fileParts = request.getParts().stream()
                        .filter(part -> "files".equals(part.getName()) && part.getSize() > 0)
                        .collect(Collectors.toList());

                for (Part filePart : fileParts) {
                    String fileName = filePart.getSubmittedFileName();
                    String contentType = filePart.getContentType();
                    byte[] fileData = filePart.getInputStream().readAllBytes();

                    File file = new File();
                    file.setFileName(fileName);
                    file.setFileData(fileData);
                    file.setContentType(contentType);
                    files.add(file);
                }

                postService.createPost(post, files, conn);
                auditLogDAO.logAction(userId, "CREATE_POST", "Created post ID: " + post.getId(), conn);
                conn.commit();
                logger.info("Post created: ID={}, userId: {}", post.getId(), userId);
                response.sendRedirect("/posts/" + post.getId());
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Database error while creating post for userId: {}", userId, e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании поста");
                throw e;
            }
        } catch (SQLException e) {
            logger.error("Failed to close connection", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}