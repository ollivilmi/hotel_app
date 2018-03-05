package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Hillo
 */
@WebServlet(name = "logout", urlPatterns = {"/logout"})
public class Logout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
            // Get session variable where we have stored our current user
            HttpSession session = request.getSession();
            
            // Remove the user attribute
            if (session != null)
            {
                session.removeAttribute("user");
            }
            // Remove the user cookie which is used for JavaScript
            for (Cookie cookie : request.getCookies())
                if (cookie.getName().equals("user"))
                {
                    cookie = null;
                    break;
                }
            // Send the user back to the login page
            response.sendRedirect("/management/login.html");
    }
}
