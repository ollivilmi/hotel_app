/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Models.Users;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import Beans.UserBean;
import Login.Password;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import service.UsersFacadeREST;

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
        String uname = request.getParameter("username");
        String pass = request.getParameter("pass");
        Users u = bean.getByUsername(uname);
        if (u != null)
            if (Password.check(pass, u.getPwHash()))
            {
                HttpSession session = request.getSession();
                session.setAttribute("user", u);
                session.setMaxInactiveInterval(30*60);

                Cookie username = new Cookie("user", uname);
                username.setMaxAge(30*60);
                response.addCookie(username);

                response.sendRedirect("/management/main");
            }
        response.sendRedirect("/management/login.html");
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
