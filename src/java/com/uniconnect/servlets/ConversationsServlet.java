package com.uniconnect.servlets;


import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/conversations")  // Mapping servlet to handle requests to /conversations endpoint
public class ConversationsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Authentication validation - ensuring user has valid session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null || 
            !(Boolean) session.getAttribute("authenticated")) {
            
            // Session is invalid or user not authenticated - redirecting to login
            response.sendRedirect("login");
            return;  // Terminating request processing for unauthorized access
        }
        
        // User authentication confirmed - proceeding with conversations page
        // Extracting username from session for personalization
        String username = (String) session.getAttribute("username");
        
        // Loading conversations HTML template from protected WEB-INF directory
        InputStream htmlStream = getServletContext().getResourceAsStream("/WEB-INF/conversations.html");
        if (htmlStream == null) {
            // Template file not found - returning 404 error response
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Conversations page not found");
            return;
        }
        
        // Reading HTML template content into memory for processing
        StringBuilder htmlContent = new StringBuilder();
        
        try (java.util.Scanner scanner = new java.util.Scanner(htmlStream, "UTF-8")) {
            while (scanner.hasNextLine()) {
                // Building complete HTML content line by line
                htmlContent.append(scanner.nextLine()).append("\n");
            }
        }
        
        // Converting to string for template variable replacement
        String processedHtml = htmlContent.toString();
        
        // Template processing - substituting username placeholder with actual value
        // Replacing '${username}' markers with user's authenticated username
        processedHtml = processedHtml.replace("'${username}'", "'" + username + "'");
        
        // Preparing HTTP response with proper headers and content
        response.setContentType("text/html");     // Setting MIME type for HTML content
        response.setCharacterEncoding("UTF-8");   // Configuring UTF-8 encoding for international characters
        response.getWriter().write(processedHtml); // Sending processed HTML to client browser
    }
}