/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hillo
 */
@SqlResultSetMapping(name="NotesDataResult", 
        entities = {
            @EntityResult(entityClass = Notes.class, 
                fields = {@FieldResult(name="id", column="id"), @FieldResult(name="contents", column="contents"),
                          @FieldResult(name="note_date", column="note_date"),  @FieldResult(name="department_id", column="department_id"), 
                          @FieldResult(name="img_url", column="img_url"), @FieldResult(name="user_id", column="user_id")})
        }
)

@SqlResultSetMapping(name="NotesTimeResult", 
        entities = {
            @EntityResult(entityClass = Notes.class, 
                fields = {@FieldResult(name="id", column="id"), @FieldResult(name="contents", column="contents"),
                          @FieldResult(name="note_date", column="note_date"),  @FieldResult(name="department_id", column="department_id"), 
                          @FieldResult(name="img_url", column="img_url"), @FieldResult(name="title", column="title")})
        }
)

@Entity
@Table(name = "Notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notes.findAll", query = "SELECT n FROM Notes n ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.findById", query = "SELECT n FROM Notes n WHERE n.id = :id ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.findByNoteDate", query = "SELECT n FROM Notes n WHERE n.noteDate = :noteDate ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.findByImgUrl", query = "SELECT n FROM Notes n WHERE n.imgUrl = :imgUrl ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.findByDepartmentId", query = "SELECT n FROM Notes n WHERE n.departmentId = :departmentId AND n.status = :status ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.getDateByNoteId", query = "SELECT n.noteDate FROM Notes n WHERE n.id = :noteId ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.orderByDate", query = "SELECT n FROM Notes n ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.forUser", query = "SELECT n FROM Notes n LEFT JOIN NoteReceivers nr ON n.id = nr.noteReceiversPK.noteId WHERE nr.noteReceiversPK.userId = :userid OR n.departmentId = :departmentid ORDER BY n.noteDate DESC")
    , @NamedQuery(name = "Notes.byTime", query = "SELECT n FROM Notes n WHERE n.noteDate > :date AND n.status = :status ORDER BY n.noteDate DESC")
})
public class Notes implements Serializable {

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "contents")
    private String contents;
    @Column(name = "note_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date noteDate;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "title")
    private String title;
    @Column(name = "status")
    private Integer status;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    public Notes() {
    }

    public Notes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notes)) {
            return false;
        }
        Notes other = (Notes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return
     "{"
    +" \"id\": \"" + this.getId() + "\"," 
    +" \"contents\": \"" + this.getContents() + "\","                  
    +" \"note_date\" : \"" + this.getNoteDate() + "\"," 
    +" \"img_url\" : \"" + this.getImgUrl() + "\"," 
    +" \"department_id\" : \"" + this.getDepartmentId() + "\""
    +"}";
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
