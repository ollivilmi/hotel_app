/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Models.NoteReceivers;
import Models.NoteReceiversPK;
import Models.Notes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Hillo
 */
@Stateless
public class NotesBean {

    @PersistenceContext(unitName = "ManagementPU")
    private EntityManager em;
    
    public void createNote(String text)
    {
        Notes n = new Notes();
        n.setContents(text);
        em.persist(n);
    }
    
    public void addReceiver(Notes note, int receiver)
    {
        NoteReceiversPK pk = new NoteReceiversPK(note.getId(), receiver);
        NoteReceivers rec = new NoteReceivers(pk);
        em.persist(rec);
    }
   
}
