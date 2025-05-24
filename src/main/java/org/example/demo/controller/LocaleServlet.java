package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/locale")
public class LocaleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String lang = req.getParameter("lang");
        if (lang != null && (lang.equals("ru") || lang.equals("en"))) {
            req.getSession().setAttribute("locale", lang);
            Cookie localeCookie = new Cookie("locale", lang);
            localeCookie.setMaxAge(30 * 24 * 60 * 60);
            resp.addCookie(localeCookie);
        }
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/posts");
    }
}