package org.launchcode.models.forms;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicUpdate
public class Service {

    @Id
    @GeneratedValue
    private int id;

    // pattern used for using html type = "date" for a calendar popup for drop-off and pick-up dates
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    // many-to-one relationship established - creates column dog_id in service table as foreign key
    @ManyToOne
    @JoinColumn(name="dog_id")
    private Dog dog;

    // many-to-one relationship established - creates column person_id in service table as foreign key
    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    public Service() { }

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

}