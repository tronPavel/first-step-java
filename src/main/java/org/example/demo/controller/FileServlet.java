package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.dao.FileDAO;
import org.example.demo.model.File;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/files/*")
public class FileServlet extends HttpServlet {
    private FileDAO fileDAO = new FileDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Укажите ID файла");
            return;
        }

        int fileId = Integer.parseInt(pathInfo.substring(1));
        try {
            File file = fileDAO.getFileById(fileId);
            if (file == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
                return;
            }

            response.setContentType(file.getContentType());
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getFileName() + "\"");
            response.getOutputStream().write(file.getFileData());
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }
}