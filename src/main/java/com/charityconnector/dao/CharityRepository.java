package com.charityconnector.dao;

import com.charityconnector.entity.Cause;
import com.charityconnector.entity.Charity;
import com.charityconnector.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CharityRepository extends JpaRepository<Charity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Charity c SET c.name = :name, c.description = :description WHERE c.id = :id")
    void updateById(@Param("id") Long id, @Param("name") String name, @Param("description") String description);

    @Query(value = "SELECT * FROM Charity c ORDER BY random() LIMIT 1;", nativeQuery = true)
    Charity findRandom();

    Charity findByOauthUserId(String oauthUserId);

    // The table name should be exactly as the entity name in the entity package
    @Query("select c from Charity c where lower(c.name) LIKE  lower(CONCAT('%',:name,'%'))")
    Charity[]  findByName(@Param("name") String name);



    //Following methods are for searching...!

    // The table name should be exactly as the entity name in the entity package
    @Query("select c from Charity c where lower(c.name) LIKE  lower(CONCAT('%',:name,'%'))")
    Page<Charity>  findByNameLike(@Param("name") String name, Pageable pageable);

    @Query("select c from Charity c where :country member of c.countries AND lower(c.name) LIKE  lower(CONCAT('%',:name,'%'))")
    Page<Charity>  findByCountry(@Param("country") Country country, @Param("name") String name, Pageable pageable);

    @Query("select c from Charity c where :cause member of c.causes AND lower(c.name) LIKE  lower(CONCAT('%',:name,'%'))")
    Page<Charity>  findByCause(@Param("cause") Cause cause, @Param("name") String name, Pageable pageable);

    @Query("select c from Charity c where :cause member of c.causes AND :country member of  c.countries  AND lower(c.name) LIKE  lower(CONCAT('%',:name,'%'))")
    Page<Charity>  findByCauseAndCountry(@Param("cause") Cause cause, @Param("country") Country country, @Param("name") String name, Pageable pageable);

    @Query("select c from Charity c where lower(c.name) LIKE lower(CONCAT('%', :stringToMatch, '%')) or lower(c.description) LIKE lower(CONCAT('%', :stringToMatch, '%'))")
    Page<Charity> findByNameOrDescriptionLike(@Param("stringToMatch") String stringToMatch ,Pageable pageable);
}
