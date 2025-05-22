package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.demo.dao.UserDAO;
import org.example.demo.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = (int) request.getSession().getAttribute("userId");
        User user = null;

        try {
            user = userDAO.getUserById(userId);
            if (user == null) {
                request.getSession().invalidate();
                response.sendRedirect("login?error=user_not_found");
                return;
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
            return;
        }

        request.setAttribute("user", user);
        renderPage(request, response, "Профиль", "/WEB-INF/views/profile.jsp");
    }
}
