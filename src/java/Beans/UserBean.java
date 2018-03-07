/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Models.Users;
import Login.Password;
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

/**
 *
 * @author Hillo
 */
@Stateless
public class UserBean {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;
    
    public int getDepartmentIdByJobId(int jobId) {
        return (int) em.createNamedQuery("Jobs.findDepartmentIdById").setParameter("id", jobId).getSingleResult();
    }
    
    public Users getByUsername(String name)
    {
        Query q = em.createNamedQuery("Users.findByUsername").setParameter("username", name);
        if (q.getResultList().isEmpty())
            return null;
        return (Users) q.getResultList().get(0);
    }
    
    public boolean updateUser(Users user)
    {
        try {
            em.merge(user);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public String getJob(Users user)
    {
        return (String) em.createNativeQuery("SELECT job.title FROM Jobs JOIN Users "
                + "ON Jobs.id = Users.job_id WHERE Users.id = " + user.getId())
                .getResultList().get(0);
    }
    
    public boolean addUser(Users user)
    {
        try { 
            em.persist(user);
        } 
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public String allUserData() {
        Query q = em.createNativeQuery("SELECT Users.id, Users.first_name, Users.last_name,"
            + "Users.username, Users.email, Users.phone_number, Jobs.title, Departments.title "
            + "FROM Users "
            + "JOIN Jobs ON Users.job_id = Jobs.id "
            + "JOIN Departments ON Jobs.department_id = Departments.id");
            return buildUserJSON(q.getResultList());
    }
    
    public String allUserDataById(int id) {
        Query q = em.createNativeQuery("SELECT Users.id, Users.first_name, Users.last_name,"
            + "Users.username, Users.email, Users.phone_number, Jobs.title, Departments.title "
            + "FROM Users "
            + "JOIN Jobs ON Users.job_id = Jobs.id "
            + "JOIN Departments ON Jobs.department_id = Departments.id "
            + "WHERE Users.id = " + id);
        return buildUserJSON(q.getResultList());
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
