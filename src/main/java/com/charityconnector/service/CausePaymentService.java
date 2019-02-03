package com.charityconnector.service;

import com.charityconnector.entity.CausePayment;

public interface CausePaymentService {

    CausePayment addPayment(CausePayment payment);

    void deleteById(Long id);

    CausePayment findById(Long id);

    void updateSelective(CausePayment payment);
}
