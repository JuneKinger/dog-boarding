package org.launchcode.models.data;

import org.launchcode.models.forms.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

// @Repository defines a repository
// Establish generic CRUD operation on repository
@Repository
@Transactional
public interface DogDao extends CrudRepository<Dog, Integer> {
    Dog findById(int id);

}

