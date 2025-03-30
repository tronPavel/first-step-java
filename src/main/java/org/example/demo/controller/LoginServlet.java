package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.model.User;
import org.example.demo.service.AuthService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private AuthService authService = new AuthService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = authService.authenticate(login, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("profile");
        } else {

            request.setAttribute("error", "Неверные учетные данные");
            renderPage(request, response, "логин", "/WEB-INF/views/login.jsp");
        }

    }
}