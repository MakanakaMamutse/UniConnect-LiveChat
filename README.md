# UniConnect Live Chat 💬

**Real-Time Communication Platform for University Environments**  
*Enterprise Programming in Java - Academic Project*

UniConnect is a comprehensive web-based messaging platform that enables students and academic staff to engage in secure, real-time conversations. Built with enterprise-grade Java technologies, this application demonstrates modern web communication patterns using WebSockets, Servlets, and dynamic client-side functionality.

<img width="1916" height="955" alt="image" src="https://github.com/user-attachments/assets/221e33f4-b6b0-4507-9d73-4acba0742149" />

## 🚀 Features

### ✅ **Completed Features**
- **🔐 User Authentication System**
  - User registration with username/password
  - Secure login with session management
  - Session-based access control

- **🏠 Personalized Home Dashboard** 
  - Welcome page with user-specific greeting
  - Clean, modern UI with glassmorphism design
  - Navigation to all platform features

- **💬 Real-Time Chat System**
  - Live messaging using WebSocket technology
  - Multi-user chat rooms with instant message delivery
  - Join/leave notifications for all participants
  - Thread-safe session management

- **🛡️ Security & Session Management**
  - Protected routes requiring authentication
  - Automatic logout functionality
  - Error handling for connection issues

### 🚧 **In Development**
- **🎥 Video Chat Integration** (Camera page framework ready)
- **📚 Study Groups** (Coming soon)

## 🏗️ Technology Stack

### **Backend Technologies**
- **Java EE 8** - Enterprise application framework
- **Java Servlets** - HTTP request/response handling
- **WebSocket API (JSR 356)** - Real-time bidirectional communication
- **GlassFish Server 5.0.1** - Application server

### **Frontend Technologies**
- **HTML5** - Semantic markup and structure
- **CSS3** - Modern styling with gradients and animations
- **JavaScript (ES6)** - Dynamic client-side functionality and WebSocket management

### **Development Environment**
- **NetBeans IDE** - Primary development environment
- **Git** - Version control system

## 🏛️ Architecture Overview

### **Servlet Architecture**
```
├── LoginServlet (/login)          - Authentication handling
├── HomeServlet (/home)            - Dashboard with user personalization  
├── ConversationsServlet (/conversations) - Chat interface
└── RegistrationServlet (/register)       - User account creation
```

### **WebSocket Architecture**
```
ChatEndpoint (/chat/{username})
├── Real-time message broadcasting
├── User session management  
├── Join/leave event handling
└── Error recovery and cleanup
```

### **Security Pattern**
- Session validation across all protected servlets
- Template-based HTML serving with user data injection
- Thread-safe WebSocket session management

## 📁 Project Structure

```
src/
├── com.uniconnect.servlets/
│   ├── LoginServlet.java
│   ├── HomeServlet.java
│   ├── ConversationsServlet.java
│   └── RegistrationServlet.java
├── com.uniconnect.websockets/
│   └── ChatEndpoint.java
└── WEB-INF/
    ├── home.html
    ├── login.html
    ├── conversations.html
    └── web.xml
```

## 🚀 Getting Started

### **Prerequisites**
- Java 8 or higher
- GlassFish Server 5.0.1
- NetBeans IDE (recommended)
- Modern web browser with WebSocket support

### **Installation & Setup**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/uniconnect.git
   cd uniconnect
   ```

2. **Import into NetBeans**
   - Open NetBeans IDE
   - File → Open Project
   - Select the UniConnect project folder

3. **Configure GlassFish Server**
   - Add GlassFish 5.0.1 to NetBeans servers
   - Ensure server is running on default port 8080

4. **Deploy Application**
   - Right-click project → Deploy
   - Access application at `http://localhost:8080/UniConnect`

### **Default Test Users**
```
Username: admin     | Password: password
Username: student1  | Password: pass123  
Username: staff1    | Password: staff123
```

## 💡 Technical Implementation Highlights

### **Real-Time Communication Architecture**

The live chat system operates on a **WebSocket-based broadcasting model** with the following technical implementation:

#### **WebSocket Connection Flow**
```
Client Request: ws://localhost:8080/UniConnect/chat/{username}
Server Response: HTTP 101 Switching Protocols → WebSocket Connection Established
```

#### **Message Broadcasting Mechanism**
```java
@ServerEndpoint("/chat/{username}")  // Dynamic endpoint with path parameter injection
```

**Session Management Strategy:**
- **Concurrent Session Storage**: `Collections.synchronizedSet(new HashSet<>())` maintains thread-safe active connections
- **Path Parameter Extraction**: `@PathParam("username")` automatically extracts username from WebSocket URL
- **Session Property Storage**: User metadata stored in `session.getUserProperties()` for message attribution

**Broadcasting Algorithm:**
1. **Message Reception**: `@OnMessage` annotation triggers when client sends data
2. **Session Iteration**: Loop through all active WebSocket sessions in synchronized collection  
3. **Connection Validation**: Check `session.isOpen()` before attempting message delivery
4. **Message Transmission**: `session.getBasicRemote().sendText()` pushes message through WebSocket tunnel
5. **Error Recovery**: Failed connections automatically removed from active session pool

**URL Endpoint Mapping:**
```
Authentication Endpoints:
├── GET/POST /login          - LoginServlet handles credential validation
├── GET/POST /register       - RegistrationServlet manages account creation
└── GET /logout              - Session invalidation and cleanup

Protected Resource Endpoints:  
├── GET /home                - HomeServlet serves personalized dashboard
├── GET /conversations       - ConversationsServlet loads chat interface
└── WebSocket /chat/{user}   - ChatEndpoint manages real-time connections
```

### **Template Processing & Dynamic Content Injection**
- **Resource Loading**: `getServletContext().getResourceAsStream()` accesses WEB-INF templates
- **Template Variable Substitution**: String replacement for `${username}` placeholders
- **Character Encoding**: UTF-8 support for international characters
- **Content-Type Headers**: Proper MIME type declaration for browser rendering

### **Session Security Pattern**
```java
HttpSession session = request.getSession(false);  // No new session creation
```
**Authentication Chain:**
1. Session existence validation
2. `authenticated` attribute verification  
3. Username extraction for personalization
4. Automatic login redirection for unauthorized access

## 🎯 Why GlassFish & Java EE 8?

While modern alternatives like **Apache Tomcat** and **Jakarta EE** exist, this project intentionally uses **GlassFish 5.0.1** and **Java EE 8** for several strategic reasons:

- **✅ Legacy System Understanding**: Many enterprise environments still run on Java EE 8
- **✅ Full Java EE Stack**: Complete container services including WebSocket support
- **✅ Educational Value**: Understanding older but stable enterprise patterns
- **✅ Project Requirements**: Specified technology constraints for learning purposes
- **✅ Production Reality**: Real-world systems often use established, proven technologies

## 🔮 Future Enhancements

- **Video Chat Integration**: WebRTC implementation for face-to-face communication
- **File Sharing**: Document and image sharing capabilities
- **Study Groups**: Private group chat functionality
- **Mobile Responsive Design**: Enhanced mobile user experience
- **Database Integration**: Persistent user and message storage
- **Push Notifications**: Browser-based notification system

## 🤝 Contributing

This is a university project demonstrating modern web communication patterns. While not actively seeking contributions, the codebase serves as a learning resource for:
- Java EE web development patterns
- WebSocket implementation strategies  
- Servlet-based authentication systems
- Real-time web application architecture

## 📚 Learning Outcomes

This project demonstrates proficiency in:
- **Enterprise Java Development** using servlets and WebSockets
- **Real-Time Web Communications** with bidirectional messaging
- **Session Management** and web application security
- **Full-Stack Integration** between server-side Java and client-side JavaScript
- **Legacy Technology Adaptation** for enterprise environments

## 📞 Contact

**Project Developer**: Makanaka Mamutse
**Course**: ITEJA3-33 - Enterprise Programming in Java  

---

*Built with ☕ Java and lots of learning*
