package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected void renderPage(HttpServletRequest request, HttpServletResponse response, String pageTitle, String contentPage)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
    }
}

