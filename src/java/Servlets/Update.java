package Servlets;

import Beans.UserBean;
import Login.Password;
import Models.Users;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hillo
 */
@WebServlet(name = "updateUser", urlPatterns = {"/updateUser"})
public class Update extends HttpServlet {

    @EJB
    private UserBean bean;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Use cookie to get user information
        Cookie user;
        for (Cookie cookie : request.getCookies())
            if (cookie.getName().equals("user"))
            {
                user = cookie;
                Users u = bean.getByUsername(user.getValue());

                // Check that the user input a correct password before making changes
                if (!Password.check(request.getParameter("password-current"), u.getPwHash()))
                    return;      

                // Get user information from the profile form
                u.setFirstName(request.getParameter("firstname"));
                u.setLastName(request.getParameter("lastname"));
                u.setUsername(request.getParameter("username"));
                u.setEmail(request.getParameter("email"));     
                u.setPhoneNumber(request.getParameter("telnumber"));

                // Double check that the required fields are not empty
                for (String parameter : u.updateForm())
                {
                    if (check(parameter))
                    {
                        response.setContentType("text/plain");
                        try (PrintWriter out = response.getWriter()) {
                            out.println("Update form missing a parameter");
                        }
                    }
                }

                // If the user wanted to change their password, double check to
                // see that they match before changing
                if (!check(request.getParameter("password")))
                    if (request.getParameter("password").equals(request.getParameter("password-repeat")))
                        u.setPwHash(Password.hash(request.getParameter("password")));

                // Everything is good, make changes to db
                bean.updateUser(u);
                response.sendRedirect("/management/secure/profilepage.html");
                break;
            }
    }
    
    // Check that a given form parameter is not null or empty
    private boolean check(String param)
    {
        return (param == null || param.isEmpty());
    }
}
