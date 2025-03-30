package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.service.AuthService;

import java.io.IOException;

@WebServlet("/signup")
public class SignUpServlet extends BaseServlet {
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderPage(req, resp, "Регистрация", "/WEB-INF/views/signup.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        try {
            authService.register(login, password, email);
            resp.sendRedirect("checkemail.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());
            renderPage(req, resp, "Регистрация", "/WEB-INF/views/signup.jsp");

        }
    }
}
