package com.charityconnector.dao;

import com.charityconnector.entity.Paypal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalRepository extends JpaRepository<Paypal, Long> {

}
