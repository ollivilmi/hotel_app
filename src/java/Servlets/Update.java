/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@WebServlet(name = "updateUser", urlPatterns = {"/updateUser"})
public class Update extends HttpServlet {

    @EJB
    private UserBean bean;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Users u = bean.getByUsername(request.getParameter("username"));
        
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
        
        u.setFirstName(request.getParameter("firstname"));
        u.setLastName(request.getParameter("lastname"));
        u.setUsername(request.getParameter("username"));
        u.setEmail(request.getParameter("email"));     
        u.setPhoneNumber(request.getParameter("telnumber"));
        
        if (!check(request.getParameter("password")))
            if (request.getParameter("password").equals(request.getParameter("password-repeat")))
                u.setPwHash(Password.hash(request.getParameter("password")));
        
        bean.updateUser(u);
        response.sendRedirect("/management/secure/profilepage.html");
    }
    
    private boolean check(String param)
    {
        return (param == null || param.isEmpty());
    }
}
