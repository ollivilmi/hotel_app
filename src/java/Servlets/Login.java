package Servlets;

import Models.Users;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Beans.UserBean;
import Login.Password;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Hillo
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {

    @EJB
    private UserBean bean;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("/management/login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get username and password from form to login
        String uname = request.getParameter("username");
        String pass = request.getParameter("password");
        
        try 
        {
            // Search by username from database to check password
            Users u = bean.getByUsername(uname);
            if (u == null)
                throw new NullPointerException();

            // Check password
            if (Password.check(pass, u.getPwHash()))
                {

                    // Create user cookie for JavaScript fetch to work
                    // Lasts 60 minutes
                    Cookie cookie = new Cookie("user", uname);
                    cookie.setMaxAge(60*60);
                    response.addCookie(cookie);

                    // Send user to the main page
                    response.sendRedirect("/management/secure/main.html");
                }
            // Passwords didn't match, send the user back to the login page
            else response.sendRedirect("/management/login.html");
        }
        catch (Exception e)
        {
            response.sendRedirect("/management/login.html");
        }
    }
}
