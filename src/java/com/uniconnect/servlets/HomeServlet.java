package com.uniconnect.servlets;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/home")  // Making this servlet listen to "/home" URL
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Authentication check - verifying user is logged in
        // Getting existing session without creating a new one
        HttpSession session = request.getSession(false);
        
        // Validating session exists and user was authenticated
        if (session == null || session.getAttribute("authenticated") == null || 
            !(Boolean) session.getAttribute("authenticated")) {
            
            // User is not authenticated - redirecting to login page
            response.sendRedirect("login");
            return;  // Stopping execution to prevent unauthorized access
        }
        
        // User is authenticated - proceeding to serve home page
        // Retrieving the username stored during login process
        String username = (String) session.getAttribute("username");
        
        // Loading the HTML template from WEB-INF directory
        InputStream htmlStream = getServletContext().getResourceAsStream("/WEB-INF/home.html");
        
        // Checking if the HTML file exists
        if (htmlStream == null) {
            // HTML template file is missing - returning 404 error
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Home page not found");
            return;
        }
        
        // Reading the HTML file content into memory
        StringBuilder htmlContent = new StringBuilder();
        // Using try-with-resources to ensure proper resource cleanup
        try (java.util.Scanner scanner = new java.util.Scanner(htmlStream, "UTF-8")) {
            while (scanner.hasNextLine()) {
                // Appending each line to build complete HTML content
                htmlContent.append(scanner.nextLine()).append("\n");
            }
        }
        
        // Converting StringBuilder to String for template processing
        String processedHtml = htmlContent.toString();
        
        // Performing template substitution - replacing placeholder with actual username
        // Searching for '${username}' placeholder and replacing with user's name
        processedHtml = processedHtml.replace("'${username}'", "'" + username + "'");
        
        // Setting response headers and sending personalized HTML to client
        response.setContentType("text/html");     // Specifying content type as HTML
        response.setCharacterEncoding("UTF-8");   // Ensuring proper character encoding
        response.getWriter().write(processedHtml); // Writing processed HTML to response stream
    }
}