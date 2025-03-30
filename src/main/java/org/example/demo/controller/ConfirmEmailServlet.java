package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.service.AuthService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/confirm")
public class ConfirmEmailServlet extends HttpServlet {
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String token = req.getParameter("token");
        if (token == null) {
            resp.sendRedirect("error.jsp");
            return;
        }

        try {
            boolean confirmed = authService.confirmRegistration(token);
            if (confirmed) {
                resp.sendRedirect("login");
            } else {

                resp.sendRedirect("error.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }
}
