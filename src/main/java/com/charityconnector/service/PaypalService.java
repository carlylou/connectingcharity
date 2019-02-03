package com.charityconnector.service;

import com.charityconnector.entity.Paypal;

public interface PaypalService {

    Paypal addPaypal(Paypal paypal);

    void deleteById(Long id);

    Paypal findById(Long id);

    void updateSelective(Paypal paypal);
}
