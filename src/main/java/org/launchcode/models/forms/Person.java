package org.launchcode.models.forms;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
public class Person {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String address;

    //@Size(min = 10, max = 10, message = "Phone # needs to be 10 digits long")
    private String homePhone;

    private String cellPhone;

    // one to many relationship established with foreign key person_id
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private List<Dog> dogNames = new ArrayList<>();


    // default constructor
    public Person() {
    }

    // parameterized constructor
    public Person(String email) {
    }

    // parameterized constructor
    public Person(String firstName,
                  String lastName, String address, String phone) {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String hPhone) {
        this.homePhone = hPhone;
    }
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cPhone) {
        this.cellPhone = cPhone;
    }

    public List<Dog> getDogNames() {
        return dogNames;
    }

    public void setDogNames(List<Dog> dogNames) {
        this.dogNames = dogNames;
    }
}