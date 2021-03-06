/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hillo
 */
@Entity
@Table(name = "Users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
    , @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName LIKE :firstName")
    , @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName LIKE :lastName")
    , @NamedQuery(name = "Users.findByPermissionsId", query = "SELECT u FROM Users u WHERE u.permissionsId = :permissionsId")
    , @NamedQuery(name = "Users.findByJobId", query = "SELECT u FROM Users u WHERE u.jobId = :jobId")
    , @NamedQuery(name = "Users.findByUsername", query ="SELECT u FROM Users u WHERE u.username LIKE :username")
    , @NamedQuery(name = "Users.findByDepartment", query ="SELECT u FROM Users u JOIN Jobs j ON u.jobId = j.id WHERE j.departmentId = :department")
    , @NamedQuery(name = "Users.findByJob", query = "SELECT u FROM Users u JOIN Jobs j ON u.jobId = j.id WHERE j.title LIKE :job")
    , @NamedQuery(name = "Users.findIdByName", query = "SELECT u.id FROM Users u WHERE u.username = :username")
    , @NamedQuery(name = "Users.findNew", query = "SELECT u FROM Users u WHERE u.permissionsId IS NULL")
    , @NamedQuery(name = "Users.unassigned", query = "SELECT u FROM Users u WHERE u.jobId IS NULL")
})
public class Users implements Serializable {

    @Column(name = "username")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Basic(optional = false)
    private Integer id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "permissions_id")
    private Integer permissionsId;
    @Column(name = "job_id")
    private Integer jobId;
    @Column(name = "pw_hash")
    private String pwHash;
    @Column(name = "picture")
    private String picture;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPermissionsId() {
        return permissionsId;
    }

    public void setPermissionsId(Integer permissionsId) {
        this.permissionsId = permissionsId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return username + " " + firstName + " " + lastName + " " + pwHash + " " + email + " " + phoneNumber;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public List<String> registrationForm()
    {
        List<String> regForm = new ArrayList<>();
        regForm.add(firstName);
        regForm.add(lastName);
        regForm.add(username);
        regForm.add(email);
        regForm.add(phoneNumber);
        regForm.add(pwHash);
        return regForm;
    }
    
    public List<String> updateForm()
    {
        List<String> updateForm = new ArrayList<>();
        updateForm.add(firstName);
        updateForm.add(lastName);
        updateForm.add(username);
        return updateForm;
    }
}
