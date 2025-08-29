package com.uniconnect.websockets;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{username}")  // Creating WebSocket endpoint with username parameter in URL
public class ChatEndpoint {
    
    // Thread-safe collection storing all active WebSocket sessions
    // Collections.synchronizedSet ensures multiple threads can safely access this
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    
    @OnOpen  // Annotation triggers when new WebSocket connection is established
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("WebSocket connection attempt from: " + username);
        
        // Adding new session to our active sessions collection
        sessions.add(session);
        // Storing username in session properties for later retrieval
        session.getUserProperties().put("username", username);
        
        // Notifying all users that someone joined the chat
        String joinMessage = username + " joined the chat";
        broadcast(joinMessage, session); // Including sender so they see their own join message
        
        System.out.println("User " + username + " connected. Total users: " + sessions.size());
    }
    
    @OnMessage  // Annotation triggers when message is received from client
    public void onMessage(String message, Session session) {
        // Retrieving username associated with this session
        String username = (String) session.getUserProperties().get("username");
        // Formatting message with username prefix
        String fullMessage = username + ": " + message;
        
        System.out.println("Message from " + username + ": " + message);
        
        // Broadcasting message to all connected clients including the sender
        broadcast(fullMessage, null);
    }
    
    @OnClose  // Annotation triggers when WebSocket connection is closed
    public void onClose(Session session) {
        // Removing session from active sessions collection
        sessions.remove(session);
        // Retrieving username for departure notification
        String username = (String) session.getUserProperties().get("username");
        
        if (username != null) {
            // Notifying remaining users that someone left
            String leaveMessage = username + " left the chat";
            broadcast(leaveMessage, null);
            
            System.out.println("User " + username + " disconnected. Total users: " + sessions.size());
        }
    }
    
    @OnError  // Annotation triggers when WebSocket error occurs
    public void onError(Session session, Throwable throwable) {
        String username = (String) session.getUserProperties().get("username");
        System.err.println("WebSocket error for user " + username + ": " + throwable.getMessage());
        throwable.printStackTrace();
        
        // Cleaning up problematic session to prevent further issues
        sessions.remove(session);
    }
    
    // Broadcasting message to all active sessions with optional exclusion
    private static void broadcast(String message, Session excludeSession) {
        // Collection to track sessions that need removal (closed or errored)
        Set<Session> sessionsToRemove = new HashSet<>();
        
        // Synchronizing access to sessions collection for thread safety
        synchronized (sessions) {
            for (Session session : sessions) {
                // Checking if session is open and not excluded from broadcast
                if (session.isOpen() && !session.equals(excludeSession)) {
                    try {
                        // Sending message to client through WebSocket connection
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        // Connection failed - marking session for removal
                        System.err.println("Error broadcasting message to session: " + e.getMessage());
                        sessionsToRemove.add(session);
                    }
                } else if (!session.isOpen()) {
                    // Session is closed - marking for cleanup
                    sessionsToRemove.add(session);
                }
            }
            
            // Removing all problematic sessions from active collection
            sessions.removeAll(sessionsToRemove);
        }
    }
}