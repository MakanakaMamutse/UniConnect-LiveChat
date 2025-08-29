package com.uniconnect.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Serving the registration page
        request.getRequestDispatcher("/WEB-INF/registration.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("register?error=username_empty");
            return;
        }
        
        if (password == null || password.length() < 6) {
            response.sendRedirect("register?error=password_short");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register?error=password_mismatch");
            return;
        }
        
        // Checking if a user already exists
        if (LoginServlet.userExists(username)) {
            response.sendRedirect("register?error=user_exists");
            return;
        }
        
        // Register the user
        LoginServlet.addUser(username, password);
        
        // Redirect to login page with success message
        response.sendRedirect("login?success=registered");
    }
}