package com.charityconnector.dao;

import com.charityconnector.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    Donor findById(Long id);

    Donor findByOauthId(String oauthId);
}
