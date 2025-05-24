package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.*;

import org.example.demo.dao.AuditLogDAO;
import org.example.demo.dao.FileDAO;
import org.example.demo.dao.PostDAO;
import org.example.demo.model.File;
import org.example.demo.model.Post;
import org.example.demo.model.User;
import org.example.demo.service.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/files/*")
public class FileServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(FileServlet.class);
    private FileDAO fileDAO = new FileDAO();
    private PostDAO postDAO = new PostDAO();
    private AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            logger.warn("Invalid file ID in request: {}", pathInfo);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID файла");
            return;
        }

        int fileId;
        try {
            fileId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            logger.error("Invalid file ID format: {}", pathInfo, e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID файла");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            File file = fileDAO.getFileById(fileId);
            if (file == null) {
                logger.warn("File not found for ID: {}", fileId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
                return;
            }

            User currentUser = (User) request.getAttribute("currentUser");
            if (currentUser == null) {
                logger.warn("Unauthorized access attempt to file ID: {}", fileId);
                response.sendRedirect("/login");
                return;
            }

            Post post = postDAO.getPostById(file.getPostId());
            if (post == null || post.getUserId() != currentUser.getId()) {
                logger.warn("Access denied for file ID: {}, user ID: {}", fileId, currentUser.getId());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
                return;
            }

            String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
            String disposition;
            boolean isDownload = "true".equals(request.getParameter("download"));
            disposition = String.format("%s; filename*=UTF-8''%s",
                    isDownload ? "attachment" : "inline", encodedFileName);

            response.setContentType(file.getContentType());
            response.setHeader("Content-Disposition", disposition);
            response.getOutputStream().write(file.getFileData());

            auditLogDAO.logAction(currentUser.getId(), isDownload ? "FILE_DOWNLOAD" : "FILE_VIEW",
                    "File ID: " + fileId + ", Name: " + file.getFileName(), conn);
            logger.info("File served successfully: ID={}, name={}, action={}", fileId, file.getFileName(),
                    isDownload ? "download" : "view");
        } catch (SQLException e) {
            logger.error("Database error while fetching file ID: {}", fileId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}