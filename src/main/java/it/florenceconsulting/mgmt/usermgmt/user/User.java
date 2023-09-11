package it.florenceconsulting.mgmt.usermgmt.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "user_email_idx", columnList = "email", unique = true),
        @Index(name = "user_fname_idx", columnList = "firstname"),
        @Index(name = "user_lname_idx", columnList = "lastname"),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "firstname", nullable = false)
    protected String firstname;

    @Column(name = "lastname", nullable = false)
    protected String lastname;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "address")
    protected String address;

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        return (this.id == null) ? 0 : this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.hashCode() == ((User)obj).hashCode();
        }

        return false;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email="
                + email + ", address=" + address + "]";
    }
}
