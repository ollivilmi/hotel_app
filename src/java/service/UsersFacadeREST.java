/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Models.Users;
import Register.Password;
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
public class UsersFacadeREST extends AbstractFacade<Users> {
    
    @EJB
    UserBean ub;

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }
    
    @POST
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    public String insertUser(@FormParam("fn") String firstName, 
            @FormParam("ln") String lastName, @FormParam("password") String password,
            @FormParam("job") int job, @FormParam("perm") int perm,
            @FormParam("email") String email, @FormParam("phone") String phone,
            @FormParam("uname") String uname)
    { 
        try 
        {
            super.create(ub.addUser(firstName, lastName, uname, password, job, perm, email, phone));
        }
        catch (Exception e)
        {
            e.toString();
        }
        return "User added";
    }
    
    @POST
    @Path("/login")
    @Produces (MediaType.TEXT_PLAIN)
    public String login(@FormParam ("username") String username, @FormParam("pass") String pass)
    {
        Users u = ub.getByUsername(username);
        if (BCrypt.checkpw(pass, u.getPwHash()))
            return "match";
        else
            return "no match";
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("/u")
    @Produces({MediaType.APPLICATION_JSON})
    public String find(@QueryParam("id") int id) {
        return ub.allUserDataById(id);
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
    public String findAllData() {
        return ub.allUserData();
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
    @Path("byDepartment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByDepartment(@QueryParam("department") int department) {
        Query q = em.createNamedQuery("Users.findByDepartment").setParameter("department", department);
        return q.getResultList();
    }
    
    @GET
    @Path("byUsername")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByDepartment(@QueryParam("username") String username) {
        Query q = em.createNamedQuery("Users.findByUsername").setParameter("username", username+"%");
        return q.getResultList();
    }
    
    @GET
    @Path("byFirstName")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByFirstName(@QueryParam("firstName") String firstName) {
        Query q = em.createNamedQuery("Users.findByFirstName").setParameter("firstName", firstName+"%");
        return q.getResultList();
    }
    
    @GET
    @Path("byLastName")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByLastName(@QueryParam("lastName") String lastName) {
        Query q = em.createNamedQuery("Users.findByLastName").setParameter("lastName", lastName+"%");
        return q.getResultList();
    }
    
    @GET
    @Path("byJobId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByJobId(@QueryParam("jobId") int jobId) {
        Query q = em.createNamedQuery("Users.findByJobId").setParameter("jobId", jobId);
        return q.getResultList();
    }
    
    @GET
    @Path("byJob")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findByJob(@QueryParam("job") String job) {
        Query q = em.createNamedQuery("Users.findByJob").setParameter("job", job+"%");
        return q.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private Response badRequest()
    {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{message\":\bad request\"}")
                .build();
    }
}
