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
import java.util.Calendar;
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
        n.setImgUrl("/management/images/"+imgUrl);
        n.setTitle(title);
        n.setStatus(0);
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
        n.setStatus(0);
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
    
    public List<Notes> getNotesByDepartmentId(int id, int status) {
        refreshEM();
        return (List<Notes>) em.createNamedQuery("Notes.findByDepartmentId").setParameter("departmentId", id).setParameter("status", status).getResultList();
    }
        
    public String getDateByNotesId(int id) {
        refreshEM();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) em.createNamedQuery("Notes.getDateByNoteId").setParameter("noteId", id).getSingleResult());
    }
    
    public void removeNoteById(int id)
    {
        refreshEM();
        Notes note = (Notes) em.createNamedQuery("Notes.findById").setParameter("id", id).getResultList().get(0);
        em.remove(note);
    }
    
    public void editNoteText(int id, String text)
    {
        refreshEM();
        Notes note = (Notes) em.createNamedQuery("Notes.findById").setParameter("id", id).getResultList().get(0);
        note.setContents(text);
        em.persist(note);
    }
    
    public List<Notes> getNotesByTime(int time, int status) {
        refreshEM();
        Date date;
        Calendar cal = Calendar.getInstance();
        switch (time)
        {
            case 0: //All
                date = new Date(0,0,0);
                break;
            case 1: //Today
                cal.add(Calendar.DAY_OF_MONTH, -1);
                date = cal.getTime();
                break;
            case 2: //This week
                cal.add(Calendar.DAY_OF_MONTH, -7);
                date = cal.getTime();
                break;
            case 3: //This month
                cal.add(Calendar.DAY_OF_MONTH, -30);
                date = cal.getTime();
                break;
            default:
                date = new Date(0,0,0);
        }
        return (List<Notes>) em.createNamedQuery("Notes.byTime").setParameter("date", date).setParameter("status", status).getResultList();
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
