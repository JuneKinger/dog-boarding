package org.launchcode.models.data;

import org.launchcode.models.forms.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface DogDao extends CrudRepository<Dog, Integer> {

}

