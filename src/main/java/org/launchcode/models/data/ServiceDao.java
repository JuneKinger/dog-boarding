package org.launchcode.models.data;

import org.launchcode.models.forms.Dog;
import org.launchcode.models.forms.Person;
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

    List<Service> findByDog_Id(int dogId);

    // @Query to define SQL to execute for a Spring Data repository method //
    @Query(value = "select * FROM service where start_date >= NOW() - INTERVAL 1 DAY order by start_date ASC", nativeQuery = true)
    List<Service> findAllByStartDateOrderByStartDateAscNative();

    @Query(value = "select * FROM service where end_date >= NOW() - INTERVAL 1 DAY order by end_date ASC", nativeQuery = true)
    List<Service> findAllByEndDateOrderByEndDateAscNative();

    @Query(value = "select * FROM service order by start_date ASC", nativeQuery = true)
    List<Service> findAllOrderByStartDateAscNative();

    @Query(value = "select * FROM service order by End_date ASC", nativeQuery = true)
    List<Service> findAllOrderByEndDateAscNative();

    //@Query(value = "select s.dog_id, s.person_id FROM service s JOIN Person p ON s.person_id = p.id order by s.start_date ASC", nativeQuery = true)
    //List<Service> findByDog_IdAndPerson_IdOrderByStartDateAscNative(int dogId, int personId);

}
