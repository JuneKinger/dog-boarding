package org.launchcode.models.data;

import org.launchcode.models.forms.Service;
import org.launchcode.models.forms.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface ServiceDao extends CrudRepository<Service, Integer> {
    Service findById(int id);

    List<Service> findByPerson_Id(int personId);

    @Query(value = "select * FROM service where start_date >= NOW() - INTERVAL 1 DAY order by start_date ASC", nativeQuery = true)
    List<Service> findAllByStartDateOrderByStartDateAscNative();

    @Query(value = "select * FROM service order by start_date ASC", nativeQuery = true)
    List<Service> findAllOrderByStartDateAscNative();
}
