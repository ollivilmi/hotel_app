/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hillo
 */
@Embeddable
public class NoteReceiversPK implements Serializable {

    @Column(name = "note_id")
    private int noteId;
    @Column(name = "user_id")
    private int userId;

    public NoteReceiversPK() {
    }

    public NoteReceiversPK(int noteId, int userId) {
        this.noteId = noteId;
        this.userId = userId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) noteId;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NoteReceiversPK)) {
            return false;
        }
        NoteReceiversPK other = (NoteReceiversPK) object;
        if (this.noteId != other.noteId) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Models.NoteReceiversPK[ noteId=" + noteId + ", userId=" + userId + " ]";
    }
    
}
