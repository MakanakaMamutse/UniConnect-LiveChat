package com.uniconnect.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null || 
            !(Boolean) session.getAttribute("authenticated")) {
            
            // Not authenticated, redirect to login
            response.sendRedirect("login");
            return;
        }
        
        // User is authenticated, serve chat page
        String username = (String) session.getAttribute("username");
        request.setAttribute("username", username);
        request.getRequestDispatcher("/WEB-INF/chat.html").forward(request, response);
    }
}