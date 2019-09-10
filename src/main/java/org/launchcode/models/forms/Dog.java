package org.launchcode.models.forms;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Dog {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=2, max=15)
    private String name;

    @NotNull
    @Size(min=2)
    private String breed;

    @NotNull
    private String size;

    @ManyToOne
    private Person person;

    public Dog() { }

    public Dog(String name, String breed, String size) {
        this.name = name;
        this.breed = breed;
        this.size = size;
    }

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
}
