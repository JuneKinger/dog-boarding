package org.launchcode.models.data;

import org.launchcode.models.forms.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

// @Repository defines a repository
// Establish generic CRUD operation on repository
// <Person, Integer> is the integer key (Id) defined for Person
@Repository
@Transactional
public interface PersonDao extends CrudRepository<Person, Integer> {
     Person findByEmail(String email);

     Person findById(int id);

}
