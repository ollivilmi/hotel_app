/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
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


@Entity
@Table(name = "Notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notes.findAll", query = "SELECT n FROM Notes n")
    , @NamedQuery(name = "Notes.findById", query = "SELECT n FROM Notes n WHERE n.id = :id")
    , @NamedQuery(name = "Notes.findByNoteDate", query = "SELECT n FROM Notes n WHERE n.noteDate = :noteDate")
    , @NamedQuery(name = "Notes.findByImgUrl", query = "SELECT n FROM Notes n WHERE n.imgUrl = :imgUrl")
    , @NamedQuery(name = "Notes.findByDepartmentId", query = "SELECT n FROM Notes n WHERE n.departmentId = :departmentId")
    , @NamedQuery(name = "Notes.getDateByNoteId", query = "SELECT n.noteDate FROM Notes n WHERE n.id = :noteId")
    , @NamedQuery(name = "Notes.orderByDate", query = "SELECT n FROM Notes n ORDER BY n.noteDate DESC")
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

}
