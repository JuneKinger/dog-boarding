package org.launchcode.models.forms;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@DynamicUpdate
public class Dog {

    @Id
    @GeneratedValue
    private int id;

    @NotEmpty
    private String name;

    private String breed;

    private String size;

    private String specialNotes;

    //@ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    private Person person;


    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL)
    private List<Service> services = new ArrayList<>();


    public Dog() { }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }


    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }


}
