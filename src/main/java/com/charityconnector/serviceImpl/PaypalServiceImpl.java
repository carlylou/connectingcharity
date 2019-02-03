package com.charityconnector.serviceImpl;

import com.charityconnector.dao.PaypalRepository;
import com.charityconnector.entity.Paypal;
import com.charityconnector.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaypalServiceImpl implements PaypalService {

    @Autowired
    PaypalRepository paypalRepository;

    @Autowired
    public PaypalServiceImpl(PaypalRepository paypalRepository) {
        this.paypalRepository = paypalRepository;
    }

    @Override
    public Paypal addPaypal(Paypal paypal) {
        Date now = new Date();
        paypal.setDate(now);
        return paypalRepository.save(paypal);
    }

    @Override
    public void deleteById(Long id) {
        paypalRepository.delete(id);
    }

    @Override
    public Paypal findById(Long id) {
        return paypalRepository.findOne(id);
    }

    @Override
    public void updateSelective(Paypal paypal) {
        Paypal readyToUpdate = new Paypal();
        Paypal origin = paypalRepository.findOne(paypal.getId());

        if (paypal.getId() == null) {
            return;
        } else {
            readyToUpdate.setId(paypal.getId());

            Date date = paypal.getDate() == null ? origin.getDate() : paypal.getDate();
            readyToUpdate.setDate(date);

            Double amount = paypal.getAmount() <= 0 ? origin.getAmount() : paypal.getAmount();
            readyToUpdate.setAmount(amount);

            long id = paypal.getCharityId() <= 0 ? origin.getCharityId() : paypal.getCharityId();
            readyToUpdate.setCharityId(id);

            String transactionId = paypal.getTransactionId() == null ? origin.getTransactionId() : paypal.getTransactionId();
            readyToUpdate.setTransactionId(transactionId);
        }
        paypalRepository.save(readyToUpdate);
    }
}
