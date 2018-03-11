/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Beans.NotesBean;
import Beans.UserBean;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author rangigo
 */
@WebServlet(urlPatterns = "/secure/r/notes/manager/new")
@MultipartConfig
public class NotesUpload extends HttpServlet {

    @EJB
    NotesBean nb;
    
    @EJB
    UserBean ub;
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        File uploads = new File("C:\\Users\\Hillo\\Desktop\\HotelManagement\\hotel_app\\web\\images"); //define storage location
        try (PrintWriter out = response.getWriter()) {
            String content = request.getParameter("content");
            String title = request.getParameter("title");
            int departmentId;
            if (request.getParameter("departmentId") == null)
                departmentId = 0;
            else departmentId = Integer.parseInt(request.getParameter("departmentId"));
            
            String fileName = null;
            
            //Get the picture/file
            Part filePart = request.getPart("file");
            if (!filePart.getSubmittedFileName().isEmpty())
            {
                fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                //create the file in the storage location
                File file = new File(uploads, fileName);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath());
                } catch (Exception ex) {
                    out.print(ex);
                }
            }
            
            //Using bean to create Notes from the POST request parameters
            Models.Notes notes;
            if (fileName == null)
                notes = nb.createNote(content, departmentId, title);
            else
                notes = nb.createNote(content, fileName, departmentId, title);
            
            //Add receiver
            try {
            String username = request.getParameter("username");
            nb.addReceiver(notes.getId(), ub.getByUsername(username).getId());
            //                out.print(notes);
            response.sendRedirect("/management/secure/main.html");
            }
            catch (Exception e)
            {
            response.sendRedirect("/management/secure/main.html");
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Notes uploading through Post request";
    }// </editor-fold>

}
