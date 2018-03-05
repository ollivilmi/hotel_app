package service;

import Beans.UserBean;
import Models.Users;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
   
    
    //----------------------------IMPORTANT--------------------------------
    //
    // Resources behind /manager/ are secured with Manager filter, and
    // use the current session to check that the user has permissions
    // to use them. They don't return any JSON, and are used mainly with
    // forms.
    //
    // The resources that return JSON use @HeaderParam("user") cookie which
    // contains the username of the current user that has been logged in.
    // To use these resources you need to get the login cookie within JS
    // and include it in the fetch header.
    //
    //---------------------------------------------------------------------
    
    @POST
    @Path("/manager/setUserPermissions")
    public void setUserPermission(@FormParam("username") String username, 
            @FormParam("perm") int perm)
    {
        Users u = ub.getByUsername(username);
        u.setPermissionsId(perm);
        super.edit(u);
    }
    
    // Manager can change user job - which also changes user's department for notes
    @POST
    @Path("/manager/setUserJob")
    public void setUserJob(@FormParam("username") String username, @FormParam("job") int job)
    {
        Users u = ub.getByUsername(username);
        u.setJobId(job);
        super.edit(u);
    }
    
    // Manager accepts registration, puts user permission level to 1 (Employee)
    // so that they can now log in
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
    
    // Get users which haven't been given permissions to log in yet
    @GET
    @Path("/getNewUsers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getNewUsers(@HeaderParam("user") String user)
    {
        if (checkManager(user))
            return em.createNamedQuery("Users.findNew").getResultList();
        return null;
    }

    // Returns a query which contains all the information needed
    // to construct the profile
    @GET
    @Path("/u")
    @Produces({MediaType.APPLICATION_JSON})
    public String find(@HeaderParam("user") String user) {
        if (check(user))
            return ub.allUserDataById(ub.getByUsername(user).getId());
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findAll(@HeaderParam("user") String user) {
        if (check(user))
            return super.findAll();
        return null;
    }
    
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public String findAllData(@HeaderParam("user") String user) {
        if (check(user))
            return ub.allUserData();
        return null;
    }
    
    //-------------------SEARCH TOOLS-------------------//
    
    // Used to search users to edit their jobs/permissions
    // and to search for note receivers.
    
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
    
    @GET
    @Path("/unassigned")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findUnassigned(@HeaderParam("user") String user)
    {
        if (checkManager(user))
            return em.createNamedQuery("Users.unassigned").getResultList();
        return null;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    // Check that the user exists before returning resources
    private boolean check(String user)
    {
        return (ub.getByUsername(user) != null);
    }
    
    // Check that the user exists and has permission level 2 or 3
    // This is mainly used to hide forms that normal employees wouldnt
    // see.
    //
    // Usage of the forms is secured with ManagerFilter which uses session.
    private boolean checkManager(String user)
    {
        Users u = ub.getByUsername(user);
        return (u != null && u.getPermissionsId() != 1);
    }
    
    
}
