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
@WebServlet(urlPatterns = "/secure/r/notes/new")
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

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        File uploads = new File("E:\\Study\\hotel_app\\Pictures"); //define storage location
        try (PrintWriter out = response.getWriter()) {
            try {
                //transform HTML String date to Java Date Object
                String date = request.getParameter("date");
                String time = request.getParameter("time");
                Date finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date + " " + time);

                Part filePart = request.getPart("file");
                String content = request.getParameter("content");
                content = content.replace("\n", "<br>");
                int departmentId = 0;
                if (!request.getParameter("departmentId").isEmpty() || request.getParameter("departmentId").equals("0")) {
                    departmentId = Integer.parseInt(request.getParameter("departmentId"));
                }

                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

                //create the file in the storage location
                File file = new File(uploads, fileName);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, file.toPath());
                } catch (Exception ex) {
                    out.print(ex);
                }

                //Using bean to create Notes from the POST request parameters
                Models.Notes notes = nb.createNote(content, finalDate, fileName, departmentId);

                if (!request.getParameter("employeeIds").isEmpty()) {
                    if (request.getParameter("employeeIds").contains(",")) {
                        String[] employeeIds = request.getParameter("employeeIds").split(",");
//                        Logger.getLogger(NotesUpload.class.getName()).log(Level.INFO, employeeIds[0], employeeIds[1]);
                        for (String id : employeeIds) {
                            nb.addReceiver(notes, Integer.parseInt(id));
                        }
                    } else {
                        nb.addReceiver(notes, Integer.parseInt(request.getParameter("employeeIds")));
                    }
                }
//                out.print(notes);
                out.print("Notes sent succesfully!");
                response.sendRedirect("/management/secure/main.html");
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
