package org.launchcode.models.forms;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private int id;

    @Email
    @Column(unique = true)
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Size(min = 6)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Address name cannot be empty")
    private String address;

    @Size(min = 10, max = 10, message = "Phone # needs to be 10 digits long")
    private String phone;

    // one to many relationship established with foreign key person_id
    @OneToMany
    @JoinColumn(name = "person_id")
    private List<Dog> dogs = new ArrayList<>();


    // default constructor
    public Person() {
    }

    // parameterized constructor
    public Person(String email, String password) {
    }

    // parameterized constructor
    public Person(String email, String password, String verify, String firstName,
                  String lastName, String address, String phone) {
    }

    public Integer getId() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Dog> getDogs() {
        return dogs;
    }


}