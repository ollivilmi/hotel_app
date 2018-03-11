/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Beans.NotesBean;
import Beans.UserBean;
import Models.Notes;
import Models.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

/**
 *
 * @author Hillo
 */
@Stateless
@Path("notes")
public class NotesResources extends AbstractFacade<Notes> {

    @EJB
    NotesBean nb;
    
    @EJB
    UserBean ub;
    
    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public NotesResources() {
        super(Notes.class);
    }
    
    @POST
    @Path("newReceiver")
    public void addReceiver(@QueryParam("noteId")int noteId, @QueryParam("username")String username) {
        nb.addReceiver(noteId, ub.getByUsername(username).getId());
    }
    
    @POST
    @Path("/manager/deleteNote")
    public void deleteNote(@FormParam("noteId") int noteId)
    {
        nb.removeNoteById(noteId);
    }
    
    @GET
    @Path("byUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getByUserId(@QueryParam("id") Integer id) {
        return nb.getNotesByUserId(id);
    }
    
    @POST
    @Path("/updateNoteStatus")
    public void updateNoteStatus(@FormParam("status") int status, @FormParam("id") int id) {
        Notes n = nb.findById(id);
        n.setStatus(status);
        em.persist(n);
    }
    
    @GET
    @Path("byUsername")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getByUsername(@QueryParam("username") String username) {
        return nb.getNotesByUsername(username);
    }
    
    @GET
    @Path("byDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getByDepartmentId(@QueryParam("departmentId")int departmentId,
            @QueryParam("status") int status) {
        return nb.getNotesByDepartmentId(departmentId, status);
    }
    
    @GET
    @Path("byTime")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getByTime(@QueryParam("time")int time, @QueryParam("status") int status) {
        return nb.getNotesByTime(time, status);
    }
    
    @GET
    @Path("getNoteDate")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDateByNoteId(@QueryParam("noteId") int noteId) {
        return nb.getDateByNotesId(noteId) + " GMT+2";
    }
    
    @GET
    @Path("getNotes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> find(@HeaderParam("user") String username) {
        if (check(username)) {
            Users user = ub.getByUsername(username);
            if (user.getPermissionsId() == 1 && user.getJobId() != null) {
                return nb.notesData(user.getId(), ub.getDepartmentIdByJobId(user.getJobId()));
            }
            else {
                return nb.newestNotes();
            }
        }
        return null;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private boolean check(String user)
    {
        return (ub.getByUsername(user) != null);
    }
}
