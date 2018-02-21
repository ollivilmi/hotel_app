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

/**
 *
 * @author Hillo
 */
@Stateless
public class UserBean {

    @PersistenceContext
    private EntityManager em;
    
    public Users getByUsername(String name)
    {
        Query q = em.createNamedQuery("Users.findByUsername").setParameter("username", name);
        return (Users) q.getResultList().get(0);
    }
    
    public Users addUser(String firstName, String lastName, String uname, String password, int job, int perm, String email, String phone)
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
        return u;
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
