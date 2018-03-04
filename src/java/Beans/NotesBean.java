/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Models.NoteReceivers;
import Models.NoteReceiversPK;
import Models.Notes;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonWriter;
import javax.persistence.Query;
/**
 *
 * @author Hillo
 */
@Stateless
public class NotesBean {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;
    
    public Notes createNote(String text, Date date, String imgUrl, int departmentId)
    {
        refreshEM();
        Notes n = new Notes();
        n.setContents(text);
        n.setNoteDate(date);
        n.setImgUrl(imgUrl);
        if (departmentId != 0) n.setDepartmentId(departmentId);  
        em.persist(n);
        return n; 
   }
    
    public void addReceiver(Notes note, int receiver)
    {
        refreshEM();
        NoteReceiversPK pk = new NoteReceiversPK(note.getId(), receiver);
        NoteReceivers rec = new NoteReceivers(pk);
        em.persist(rec);
    }
    
    
    public Notes getByImgUrl(String imgUrl) {
        refreshEM();
        return (Notes) em.createNamedQuery("Notes.findByImgUrl").setParameter("imgUrl", imgUrl).getSingleResult();
    }
    
    public List<Notes> getNotesByUserId(int userId) {
        refreshEM();
        
        List<Integer> noteIds = em.createNamedQuery("NoteReceivers.findByUserId").setParameter("userId", userId).getResultList();
        List<Notes> results = new ArrayList<>(noteIds.size());
        
        for (int id : noteIds) {
            results.add((Notes) em.createNamedQuery("Notes.findById").setParameter("id", id).getSingleResult());
        }
        
        return results;
    }
    
    public List<Notes> getNotesByUsername(String name) {
        refreshEM();
        int id = (int) em.createNamedQuery("Users.findIdByName").setParameter("username", name).getSingleResult();
        return getNotesByUserId(id);
    }
    
    public List<Notes> getNotesByDepartment(String departmentName) {
        refreshEM();
        int id = (int) em.createNamedQuery("Departments.findIdByTitle").setParameter("title", departmentName).getSingleResult();
        return (List<Notes>) em.createNamedQuery("Notes.findByDepartmentId").setParameter("departmentId", id).getResultList();
    }
    
    public String getDateByNotesId(int id) {
        refreshEM();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) em.createNamedQuery("Notes.getDateByNoteId").setParameter("noteId", id).getSingleResult());
    }
    
    
    /*
    This method will just empty the cache,
    so if you fetch objects changed outside the entity manager, 
    it will do an actual database query instead of using the outdated cached value.
    */
    public void refreshEM() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
    
    public String notesData(int userId, int departmentId) {
        Query q = em.createNativeQuery("SELECT * FROM Notes "
                + "LEFT JOIN Note_Receivers ON id = note_id "
                + "WHERE user_id = " + userId + " OR department_id = "+ departmentId + " ORDER BY note_date;"
        );
        
        return buildUserJSON(q.getResultList());
    }
    
    private String buildUserJSON(List<Object[]> results)
    {
        JsonBuilderFactory jf = Json.createBuilderFactory(null);
        StringWriter sWriter = new StringWriter();
        JsonArrayBuilder jb = jf.createArrayBuilder();
        
        for (Object[] n : results)
        {
            jb
                .add(jf.createObjectBuilder()
                    .add("id", n[0].toString())
                    .add("contents", n[1].toString())
                    .add("note_date", n[2].toString())
                    .add("department_id", n[3].toString())
                    .add("img_url", n[4].toString())
                    .add("user_id", n[5].toString())
                );
        }
        
        JsonArray jay = jb.build();

        try (JsonWriter jWriter = Json.createWriter(sWriter)) {
            jWriter.write(jay);
        }
        return sWriter.toString();
    }
}
