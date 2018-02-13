/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hillo
 */
@Entity
@Table(name = "Note_Receivers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NoteReceivers.findAll", query = "SELECT n FROM NoteReceivers n")
    , @NamedQuery(name = "NoteReceivers.findByNoteId", query = "SELECT n FROM NoteReceivers n WHERE n.noteReceiversPK.noteId = :noteId")
    , @NamedQuery(name = "NoteReceivers.findByUserId", query = "SELECT n FROM NoteReceivers n WHERE n.noteReceiversPK.userId = :userId")
    , @NamedQuery(name = "NoteReceivers.findByDepartmentId", query = "SELECT n FROM NoteReceivers n WHERE n.departmentId = :departmentId")})
public class NoteReceivers implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "department_id")
    private int departmentId;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NoteReceiversPK noteReceiversPK;

    public NoteReceivers() {
    }

    public NoteReceivers(NoteReceiversPK noteReceiversPK) {
        this.noteReceiversPK = noteReceiversPK;
    }

    public NoteReceivers(NoteReceiversPK noteReceiversPK, int departmentId) {
        this.noteReceiversPK = noteReceiversPK;
        this.departmentId = departmentId;
    }

    public NoteReceivers(int noteId, int userId) {
        this.noteReceiversPK = new NoteReceiversPK(noteId, userId);
    }

    public NoteReceiversPK getNoteReceiversPK() {
        return noteReceiversPK;
    }

    public void setNoteReceiversPK(NoteReceiversPK noteReceiversPK) {
        this.noteReceiversPK = noteReceiversPK;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noteReceiversPK != null ? noteReceiversPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NoteReceivers)) {
            return false;
        }
        NoteReceivers other = (NoteReceivers) object;
        if ((this.noteReceiversPK == null && other.noteReceiversPK != null) || (this.noteReceiversPK != null && !this.noteReceiversPK.equals(other.noteReceiversPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.NoteReceivers[ noteReceiversPK=" + noteReceiversPK + " ]";
    }
    
}
