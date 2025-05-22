package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.demo.dao.FileDAO;
import org.example.demo.dao.PostDAO;
import org.example.demo.model.File;
import org.example.demo.model.Post;
import org.example.demo.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({"/posts", "/posts/new"})
@MultipartConfig(
        maxFileSize = 16 * 1024 * 1024,
        maxRequestSize = 64 * 1024 * 1024
)
public class PostsServlet extends BaseServlet {
    private PostDAO postDAO = new PostDAO();
    private FileDAO fileDAO = new FileDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        String path = req.getServletPath();
        if ("/posts/new".equals(path)) {
            renderPage(req, res, "Создать пост", "/WEB-INF/views/create.jsp");
        } else {
            try {
                List<Post> posts = postDAO.getPostsByUserId(userId);
                req.setAttribute("posts", posts);
                renderPage(req, res, "Посты", "/WEB-INF/views/posts.jsp");
            } catch (SQLException e) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUserId(userId);

        try {
            postDAO.createPost(post);

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
                file.setPostId(post.getId());
                fileDAO.createFile(file);
            }

            response.sendRedirect("/posts/" + post.getId());
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании поста");
        }
    }
}