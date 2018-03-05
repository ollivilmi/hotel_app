/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Beans.UserBean;
import Models.Users;
import Login.Password;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonWriter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import javax.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Hillo
 */
@Stateless
@Path("users")
public class UserResources extends AbstractFacade<Users> {
    
    @EJB
    UserBean ub;

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public UserResources() {
        super(Users.class);
    }
   
    @POST
    @Path("/manager/setUserPermissions")
    public void setUserPermission(@FormParam("username") String username, 
            @FormParam("perm") int perm)
    {
        Users u = ub.getByUsername(username);
        u.setPermissionsId(perm);
        super.edit(u);
    }
    
    //Manager can change user job - which also changes user's department for notes
    @POST
    @Path("/manager/setUserJob")
    public void setUserJob(@FormParam("username") String username, @FormParam("job") int job)
    {
        Users u = ub.getByUsername(username);
        u.setJobId(job);
        super.edit(u);
    }
    
    //Manager accepts registration, puts user permission level to 1 (Employee)
    //so that they can now log in
    @POST
    @Path("/manager/acceptUser")
    public void acceptUser(@FormParam("username") String username, @FormParam("accept") String accept)
    {
        if (accept != null)
        {
            Users u = ub.getByUsername(username);
            u.setPermissionsId(1);
            super.edit(u);
        }
        else //Manager declines new registration request, deletes user from database
        {
            Users u = ub.getByUsername(username);
            super.remove(u);
        }
    }
    
    @GET
    @Path("/getNewUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getNewUsers(@HeaderParam("user") String user)
    {
        if (checkManager(user))
            return em.createNamedQuery("Users.findNew").getResultList();
        return null;
    }

    @GET
    @Path("/u")
    @Produces({MediaType.APPLICATION_JSON})
    public String find(@HeaderParam("user") String user) {
        if (check(user))
            return ub.allUserDataById(ub.getByUsername(user).getId());
        return null;
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public String findAllData(@HeaderParam("user") String user) {
        if (check(user))
            return ub.allUserData();
        return null;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("/byDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByDepartment(@QueryParam("department") int department,
            @HeaderParam("user") String user) {
        if(check(user))
            return em.createNamedQuery("Users.findByDepartment").setParameter("department", department).getResultList();
        return null;
    }
    
    @GET
    @Path("/byUsername")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByUserName(@QueryParam("username") String username, @HeaderParam("user") String user) {
        if(check(user))
            return em.createNamedQuery("Users.findByUsername").setParameter("username", username+"%").getResultList();
        return null;
    }
    
    @GET
    @Path("/byFirstName")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByFirstName(@QueryParam("firstName") String firstName, 
            @HeaderParam("user") String user) {
        if (check(user))
            return em.createNamedQuery("Users.findByFirstName").setParameter("firstName", firstName+"%").getResultList();
        return null;
    }
    
    @GET
    @Path("/byLastName")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByLastName(@QueryParam("lastName") String lastName,
            @HeaderParam("user") String user) {
        if (check(user))
            return em.createNamedQuery("Users.findByLastName").setParameter("lastName", lastName+"%").getResultList();
        return null;
    }
    
    @GET
    @Path("/byJobId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByJobId(@QueryParam("jobId") int jobId, @HeaderParam("user") String user) {
        if (check(user))
            return em.createNamedQuery("Users.findByJobId").setParameter("jobId", jobId).getResultList();
        return null;
    }
    
    @GET
    @Path("/byJob")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByJob(@QueryParam("job") String job, @HeaderParam("user") String user) {
        if (check(user))
            return em.createNamedQuery("Users.findByJob").setParameter("job", job+"%").getResultList();
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
    
    private boolean checkManager(String user)
    {
        Users u = ub.getByUsername(user);
        return (u != null && u.getPermissionsId() != 1);
    }
    
    
}
