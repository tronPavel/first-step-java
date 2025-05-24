package org.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.dao.UserDAO;
import org.example.demo.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

@WebFilter(urlPatterns = {"/profile", "/posts/new", "/posts/*", "/files/*"})
public class AuthFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);
    private UserDAO userDAO = new UserDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            logger.warn("Unauthorized access to {}, redirecting to login", req.getRequestURI());
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        try {
            User user = userDAO.getUserById(userId);
            if (user == null || !"ACTIVE".equals(user.getStatus())) {
                logger.warn("Invalid user or inactive status for userId: {}", userId);
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login?error=invalid_user");
                return;
            }

            req.setAttribute("currentUser", user);
            logger.debug("Added user {} to request attributes", user.getLogin());

            if (req.getRequestURI().startsWith(req.getContextPath() + "/admin")) {
                String role = user.getRole() != null ? user.getRole() : "USER";
                if (!"ADMIN".equals(role)) {
                    logger.warn("User {} lacks admin rights for {}", user.getLogin(), req.getRequestURI());
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён");
                    return;
                }
            }

            chain.doFilter(req, response);
        } catch (SQLException e) {
            logger.error("Database error in AuthFilter for userId: {}", userId, e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("AuthFilter initialized");
    }

    @Override
    public void destroy() {
        logger.info("AuthFilter destroyed");
    }
}