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
import javax.persistence.Query;
/**
 *
 * @author Hillo
 */
@Stateless
public class NotesBean {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;
    
    public Notes createNote(String text, String imgUrl, int departmentId, String title)
    {
        refreshEM();
        Notes n = new Notes();
        n.setContents(text);
        n.setImgUrl(imgUrl);
        n.setTitle(title);
        if (departmentId != 0) n.setDepartmentId(departmentId);  
        em.persist(n);
        return n; 
   }
    
    public Notes createNote(String text, int departmentId, String title)
    {
        refreshEM();
        Notes n = new Notes();
        n.setContents(text);
        n.setTitle(title);
        n.setImgUrl(null);
        if (departmentId != 0) n.setDepartmentId(departmentId);  
        em.persist(n);
        return n; 
   }
    
    public void addReceiver(int noteId, int receiver)
    {
        refreshEM();
        NoteReceiversPK pk = new NoteReceiversPK(noteId, receiver);
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
    
    public List<Notes> getNotesByDepartmentId(int id) {
        refreshEM();
        return (List<Notes>) em.createNamedQuery("Notes.findByDepartmentId").setParameter("departmentId", id).getResultList();
    }
        
    public String getDateByNotesId(int id) {
        refreshEM();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) em.createNamedQuery("Notes.getDateByNoteId").setParameter("noteId", id).getSingleResult());
    }
    
    public List<Notes> getNotesByTime(int time) {
        refreshEM();
        Query q;
        switch (time) {
            case 7:
                q = em.createNativeQuery("SELECT * FROM Notes "
                        + "WHERE DATE_SUB(CURDATE(),INTERVAL 7 DAY) <= note_date "
                        + "ORDER BY note_date DESC;", "NotesTimeResult");
                return (List<Notes>)q.getResultList();
            case 1:
                q = em.createNativeQuery("SELECT * FROM Notes "
                        + "WHERE DATE_SUB(CURDATE(),INTERVAL 30 DAY) <= note_date "
                        + "ORDER BY note_date DESC;", "NotesTimeResult");
                return (List<Notes>)q.getResultList();
            case 0:
                q = em.createNativeQuery("SELECT * FROM Notes "
                        + "WHERE note_date = CURDATE()"
                        + "ORDER BY note_date DESC;", "NotesTimeResult");
                return (List<Notes>)q.getResultList();
            default:
                return newestNotes();
        }
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
        refreshEM();
        return em.createNamedQuery("Notes.forUser").setParameter("userid", userId).setParameter("departmentid", departmentId).getResultList();
    }
    
    public Notes findById(int noteId) {
        refreshEM();
        return (Notes) em.createNamedQuery("Notes.findById").setParameter("id", noteId).getResultList().get(0);
    }
    
    public List<Notes> newestNotes() {
        refreshEM();
        return (List<Notes>) em.createNamedQuery("Notes.orderByDate").getResultList();
    }
    
}   
