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
import org.jboss.logging.Logger;
/**
 *
 * @author Hillo
 */
@Stateless
public class NotesBean {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;
    
    public Notes createNote(String text, String imgUrl, int departmentId)
    {
        refreshEM();
        Notes n = new Notes();
        n.setContents(text);
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
    
    public List<Notes> notesData(int userId, int departmentId) {
        Query q = em.createNativeQuery("SELECT n.id AS id, n.contents AS contents, n.note_date AS note_date,"
                + " n.department_id AS department_id, n.img_url AS img_url, nr.user_id AS user_id "
                + "FROM Notes n, Note_Receivers nr "
                + "WHERE n.id = nr.note_id AND "
                + "(user_id = " + userId + " OR department_id = " + departmentId + ") ORDER BY note_date;", "NotesDataResult");
        
        return (List<Notes>) q.getResultList();
    }

}
