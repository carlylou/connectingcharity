package com.charityconnector.serviceImpl;

import com.charityconnector.dao.CausePaymentRepository;
import com.charityconnector.entity.CausePayment;
import com.charityconnector.service.CausePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CausePaymentServiceImpl implements CausePaymentService {

    CausePaymentRepository causePaymentRepository;

    @Autowired
    public CausePaymentServiceImpl(CausePaymentRepository causePaymentRepository) {
        this.causePaymentRepository = causePaymentRepository;
    }

    @Override
    public CausePayment addPayment(CausePayment payment) {
        Date now = new Date();
        payment.setDate(now);
        return causePaymentRepository.save(payment);
    }

    @Override
    public void deleteById(Long id) {
        causePaymentRepository.delete(id);
    }

    @Override
    public CausePayment findById(Long id) {
        return causePaymentRepository.findOne(id);
    }

    @Override
    public void updateSelective(CausePayment payment) {
        CausePayment readyToUpdate = new CausePayment();
        CausePayment origin = causePaymentRepository.findOne(payment.getId());

        if (payment.getId() == null) {
            return;
        } else {
            readyToUpdate.setId(payment.getId());

            Date date = payment.getDate() == null ? origin.getDate() : payment.getDate();
            readyToUpdate.setDate(date);

            Double amount = payment.getAmount() <= 0 ? origin.getAmount() : payment.getAmount();
            readyToUpdate.setAmount(amount);

            long id = payment.getCauseId() <= 0 ? origin.getCauseId() : payment.getCauseId();
            readyToUpdate.setCauseId(id);

            String transactionId = payment.getTransactionId() == null ? origin.getTransactionId() : payment.getTransactionId();
            readyToUpdate.setTransactionId(transactionId);
        }
        causePaymentRepository.save(readyToUpdate);
    }
}
