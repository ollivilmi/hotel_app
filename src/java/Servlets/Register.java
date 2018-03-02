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
import java.util.ArrayList;
import java.util.List;
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
        
        Users u = new Users();
        u.setFirstName(request.getParameter("firstname"));
        u.setLastName(request.getParameter("lastname"));
        u.setUsername(request.getParameter("username"));
        u.setEmail(request.getParameter("email"));
        u.setPhoneNumber(request.getParameter("telnumber"));
        u.setPwHash(Password.hash(request.getParameter("password")));

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
        if (bean.addUser(u))
            response.sendRedirect("/management/login.html");
        else
            response.sendRedirect("/management/error.html");
    }
    
    private boolean check(String param)
    {
        return (param == null || param.isEmpty());
    }
}
