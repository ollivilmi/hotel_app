/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Beans.NotesBean;
import Beans.UserBean;
import Models.Notes;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Hillo
 */
@Stateless
@Path("notes")
public class NotesFacadeREST extends AbstractFacade<Notes> {

    @EJB
    NotesBean nb;
    
    @EJB
    UserBean ub;
    
    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public NotesFacadeREST() {
        super(Notes.class);
    }

//
//    @POST
//    @Override
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void create(Notes entity) {
//        super.create(entity);
//    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Notes entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("byUserId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> getByUserId(@QueryParam("id") Integer id) {
        return nb.getNotesByUserId(id);
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
    public List<Notes> getByDepartment(@QueryParam("department") String department) {
        return nb.getNotesByDepartment(department);
    }
    
    @GET
    @Path("getNoteDate")
    @Produces(MediaType.TEXT_PLAIN)
    public String getByDepartment(@QueryParam("noteId") int noteId) {
        return nb.getDateByNotesId(noteId) + " GMT+2";
    }
    
    @GET
    @Path("getNotes")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@HeaderParam("user") String username) {
        if (check(username)) {
            Models.Users user = ub.getByUsername(username);
            if (user.getPermissionsId() == 1) {
                return nb.notesData(user.getId(), ub.getDepartmentIdByJobId(user.getJobId()));
            }
        }
        return null;
    }
    
    //TESTING
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public int test(String username) {
            Models.Users user = ub.getByUsername(username);
            if (user.getPermissionsId() == 1) {
                return ub.getDepartmentIdByJobId(user.getJobId());
            }
            return 0;
    }
    
    @GET 
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Notes> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
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
