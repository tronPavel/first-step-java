package org.example.demo;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "numberServlet", value = "/double")
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            String numberParam = request.getParameter("number");
            if (numberParam == null || numberParam.isEmpty()) {
                out.println("<h1>Введите число в URL, например: ?number=5</h1>");
                return;
            }

            int number = Integer.parseInt(numberParam);
            int result = number * 2;

            logger.info("Получено число: {}, результат: {}", number, result);

            out.println("<html><body>");
            out.println("<h1>Результат: " + result + "</h1>");
            out.println("</body></html>");
        } catch (NumberFormatException e) {
            logger.error("Ошибка преобразования числа", e);
            out.println("<h1>Ошибка: Введите корректное число</h1>");
        }
    }
}

