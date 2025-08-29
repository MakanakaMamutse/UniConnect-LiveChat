package com.uniconnect.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    // In-memory user storage (in production, you must use a database)
    private static Map<String, String> users = new HashMap<>();
    
    static {
        // Add some default users for testing
        users.put("admin", "password");
        users.put("student1", "pass123");
        users.put("staff1", "staff123");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Serve the login page
        request.getRequestDispatcher("/WEB-INF/login.html").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate credentials
        if (username != null && password != null && 
            users.containsKey(username) && users.get(username).equals(password)) {
            
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("authenticated", true); // Saves data so other servlets can retrieve it later
            
            // Redirect to home page
            response.sendRedirect("home");
            
        } else {
            // Invalid credentials - redirect back to login with error
            response.sendRedirect("login?error=invalid");
        }
    }
    
    // Method to add users (called by RegistrationServlet)
    public static void addUser(String username, String password) {
        users.put(username, password);
    }
    
    // Method to check if user exists
    public static boolean userExists(String username) {
        return users.containsKey(username);
    }
}