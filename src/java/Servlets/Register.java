package Servlets;

import Beans.UserBean;
import Login.Password;
import Models.Users;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hillo
 */
@WebServlet(name = "register", urlPatterns = {"/register"})
public class Register extends HttpServlet {

    @EJB
    private UserBean bean;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Double check that the user's password match
        if (!request.getParameter("password").equals(request.getParameter("password-repeat")))
        {
            response.sendRedirect("/management/error.html");
            return;
        }
        
        // Get user information from the profile form
        Users u = new Users();
        u.setFirstName(request.getParameter("firstname"));
        u.setLastName(request.getParameter("lastname"));
        u.setUsername(request.getParameter("username"));
        u.setEmail(request.getParameter("email"));
        u.setPhoneNumber(request.getParameter("telnumber"));
        u.setPwHash(Password.hash(request.getParameter("password")));

        // Double check that the required fields are not empty
        for (String parameter : u.registrationForm())
        {
            if (check(parameter))
            {
                response.setContentType("text/plain");
                try (PrintWriter out = response.getWriter()) {
                    out.println("Registration form missing a parameter");
                }
            }
        }
        
        // If everything is good, make changes to db
        if (bean.addUser(u))
            response.sendRedirect("/management/login.html");
        else
            response.sendRedirect("/management/error.html");
    }
    
    // Check that a given form parameter is not null or empty
    private boolean check(String param)
    {
        return (param == null || param.isEmpty());
    }
}
