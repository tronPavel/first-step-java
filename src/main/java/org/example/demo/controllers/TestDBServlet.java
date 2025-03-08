package org.example.demo.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.config.DBConnection;

//TODO удалить после теста
@WebServlet("/testDB")
public class TestDBServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection()) {
            resp.getWriter().println("Подключение к базе данных успешно!");
        } catch (SQLException e) {
            resp.getWriter().println(" Ошибка подключения: " + e.getMessage());
        }
    }
}
