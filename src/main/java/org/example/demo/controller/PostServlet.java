package org.example.demo.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.dao.FileDAO;
import org.example.demo.dao.PostDAO;
import org.example.demo.model.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/posts/*")
public class PostServlet extends BaseServlet {
    private PostDAO postDAO = new PostDAO();
    private FileDAO fileDAO = new FileDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID поста");
            return;
        }

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("/login");
            return;
        }

        String[] parts = pathInfo.split("/");
        int id = Integer.parseInt(parts[1]);
        Post post = null;
        try {
            post = postDAO.getPostById(id);
            if (post != null) {
                post.setFiles(fileDAO.getFilesByPostId(id));
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
            return;
        }

        if (post == null || post.getUserId() != userId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
            return;
        }

        request.setAttribute("post", post);
        if (parts.length > 2 && "edit".equals(parts[2])) {
            renderPage(request, response, "Редактировать пост", "/WEB-INF/views/edit.jsp");
        } else {
            renderPage(request, response, "Пост", "/WEB-INF/views/post.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID поста");
            return;
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        String[] parts = pathInfo.split("/");
        int id = Integer.parseInt(parts[1]);
        Post post = null;
        try {
            post = postDAO.getPostById(id);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
            return;
        }

        if (post == null || post.getUserId() != userId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
            return;
        }

        if (parts.length > 2 && "delete".equals(parts[2])) {

            try {
                postDAO.deletePost(id);
                response.sendRedirect("/posts");
            } catch (SQLException e) {
              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        } else {
            post.setTitle(request.getParameter("title"));
            post.setContent(request.getParameter("content"));
            try {
                postDAO.updatePost(post);
                response.sendRedirect("/posts/" + id);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при обновлении");
            }
        }
    }
}