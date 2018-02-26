/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Beans.NotesBean;
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
@WebServlet(urlPatterns = "/r/notes/new")
@MultipartConfig
public class NotesUpload extends HttpServlet {

    @EJB
    NotesBean nb;

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

        response.setContentType("application/json");
        
        File uploads = new File("E:\\Study\\hotel_app\\Pictures"); //define storage location
        try (PrintWriter out = response.getWriter()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date")); //transform HTML String date to Java Date Object
                Part filePart = request.getPart("file");
                String content = request.getParameter("content");
                int departmentId = Integer.parseInt(request.getParameter("departmentId"));
                
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                
                //create the file in the storage location
                File file = File.createTempFile("imgUrl_", fileName, uploads); 
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    out.print(ex);
                }
                
                //Using bean to create Notes from the POST request parameters
                Models.Notes notes =  nb.createNote(content, date, fileName, departmentId);
                nb.addReceiver(notes, 1);
                out.print(notes);
            } catch (ParseException ex) {
                Logger.getLogger(NotesUpload.class.getName()).log(Level.SEVERE, null, ex);
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
