package org.launchcode.models.data;

import org.launchcode.models.forms.Service;
import org.launchcode.models.forms.Service;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ServiceDao extends CrudRepository<Service, Integer> {
    Service findById(int id);
    List<Service> findByDog_Id(int dogId);
    List<Service> findByPerson_Id(int personId);
}
