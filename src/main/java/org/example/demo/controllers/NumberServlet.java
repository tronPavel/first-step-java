package org.example.demo.controllers;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet(value = "/number")
public class NumberServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(NumberServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int number = Integer.parseInt(request.getParameter("number"));
            int result = number * 2;
            logger.info("Получено число: {}, результат: {}", number, result);
            request.setAttribute("result", result);
        } catch (NumberFormatException e) {
            logger.error("Ошибка преобразования числа", e);
            request.setAttribute("error", "Неверные данные");
        } finally {
            request.getRequestDispatcher("number.jsp").forward(request, response);
        }
    }
}

