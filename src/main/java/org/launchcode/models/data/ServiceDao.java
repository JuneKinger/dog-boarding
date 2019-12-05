package org.launchcode.models.data;

import org.launchcode.models.forms.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

// @Repository defines a repository
// Establish generic CRUD operation on repository
@Repository
@Transactional
public interface ServiceDao extends CrudRepository<Service, Integer> {
    Service findById(int id);

    List<Service> findByPerson_Id(int personId);

    // @Query to define SQL to execute for a Spring Data repository method //
    // find future services for boarders by drop-off date
    @Query(value = "select * FROM service where start_date >= NOW() - INTERVAL 1 DAY order by start_date ASC", nativeQuery = true)
    List<Service> findAllByStartDateOrderByStartDateAscNative();

    // find future services for boarders by pick-up date
    @Query(value = "select * FROM service where end_date >= NOW() - INTERVAL 1 DAY order by end_date ASC", nativeQuery = true)
    List<Service> findAllByEndDateOrderByEndDateAscNative();

    // find all services for boarders by drop-off date
    @Query(value = "select * FROM service order by start_date ASC", nativeQuery = true)
    List<Service> findAllOrderByStartDateAscNative();

    // find all services for boarders by pick-up date
    @Query(value = "select * FROM service order by End_date ASC", nativeQuery = true)
    List<Service> findAllOrderByEndDateAscNative();

}
