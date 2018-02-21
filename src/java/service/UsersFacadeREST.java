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
import javax.ejb.Stateless;
import javax.json.Json;
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

/**
 *
 * @author Hillo
 */
@Stateless
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }
    
    @POST
    @Path("{fn}&{ln}&{pw}&{job}&{perm}")
    @Produces(MediaType.TEXT_PLAIN)
    public void insertUser(@FormParam("fn") String firstName, 
            @FormParam("ln") String lastName, @FormParam("pw") String password,
            @FormParam("job") int job, @FormParam("perm") int perm,
            @FormParam("email") String email, @FormParam("phone") String phone,
            @FormParam("uname") String uname)
    { 
        Users u = new Users();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setUsername(uname);
        String hashedPw = Password.hashPassword(password);
        u.setPwHash(hashedPw);
        u.setJobId(job);
        u.setPermissionsId(perm);
        u.setEmail(email);
        u.setPhoneNumber(phone);
        super.create(u);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Users entity) {
        super.edit(entity);
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
        Query q = em.createNativeQuery("SELECT Users.id, Users.first_name, Users.last_name,"
            + "Users.username, Users.email, Users.phone_number, Jobs.title, Departments.title "
            + "FROM Users "
            + "JOIN Jobs ON Users.job_id = Jobs.id "
            + "JOIN Departments ON Jobs.department_id = Departments.id "
            + "WHERE Users.id = " + id);
        return buildUserJSON(q.getResultList());
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
        Query q = em.createNativeQuery("SELECT Users.id, Users.first_name, Users.last_name,"
            + "Users.username, Users.email, Users.phone_number, Jobs.title, Departments.title "
            + "FROM Users "
            + "JOIN Jobs ON Users.job_id = Jobs.id "
            + "JOIN Departments ON Jobs.department_id = Departments.id");
        
        return buildUserJSON(q.getResultList());
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private String buildUserJSON(List<Object[]> results)
    {
        JsonBuilderFactory jf = Json.createBuilderFactory(null);
        StringWriter sWriter = new StringWriter();
        JsonArrayBuilder jb = jf.createArrayBuilder();
        
        for (Object[] u : results)
        {
            jb
                .add(jf.createObjectBuilder()
                    .add("id", u[0].toString())
                    .add("first_name", u[1].toString())
                    .add("last_name", u[2].toString())
                    .add("username", u[3].toString())
                    .add("email", u[4].toString())
                    .add("phone", u[5].toString())
                    .add("job", u[6].toString())
                    .add("department", u[7].toString())
                );
        }
        
        JsonArray jay = jb.build();

        try (JsonWriter jWriter = Json.createWriter(sWriter)) {
            jWriter.write(jay);
        }
        return sWriter.toString();
    }
}
