/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Models.NoteReceivers;
import Models.NoteReceiversPK;
import Models.Notes;
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
    
    public void refreshEM() {
        em.getEntityManagerFactory().getCache().evictAll();
    }
}
