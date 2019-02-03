package com.charityconnector.dao;

import com.charityconnector.entity.Cause;
import com.charityconnector.entity.Charity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CauseRepository extends JpaRepository<Cause, Long> {


    // The table name should be exactly as the entity name in the entity package
    @Query("select c from Cause c where c.name LIKE  CONCAT('%',:name,'%')")
    Cause findCauseByName(@Param("name")  String name);
}
