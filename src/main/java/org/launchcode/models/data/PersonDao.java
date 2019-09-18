package org.launchcode.models.data;

import org.launchcode.models.forms.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.io.StreamCorruptedException;

// Establish generic CRUD operation on repository
@Repository
@Transactional
public interface PersonDao extends CrudRepository<Person, Integer> {
     Person findByEmail(String email);

}
