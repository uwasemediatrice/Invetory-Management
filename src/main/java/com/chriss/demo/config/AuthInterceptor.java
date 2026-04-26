package com.chriss.demo.config;

import com.chriss.demo.model.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        AppUser currentUser = session == null ? null : (AppUser) session.getAttribute("currentUser");

        if (currentUser != null) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
