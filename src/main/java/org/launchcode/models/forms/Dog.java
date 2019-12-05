package org.launchcode.models.forms;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

//The @Entity annotation specifies that the class is an entity and is mapped to a database table.
@Entity
// @DynamicUpdate is a class-level annotation that can be applied to a JPA entity. It ensures that
// Hibernate uses only the modified columns in the SQL statement that it generates for the update of an
// entity.
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

    @ManyToOne
    private Person person;

    // mappedby makes a relationship bidirectional - dog and services
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