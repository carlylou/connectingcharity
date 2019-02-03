package com.charityconnector.controller;

import com.charityconnector.entity.CausePayment;
import com.charityconnector.entity.Charity;
import com.charityconnector.entity.Paypal;
import com.charityconnector.service.CausePaymentService;
import com.charityconnector.service.CauseService;
import com.charityconnector.service.DonorService;
import com.charityconnector.service.PaypalService;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class PaypalController {

    private static final String CLIENT_ID = "AZ4rzAJMd3YLKr5-Av9DuqvF0LdsnIN65aRFQpbNF78-jATGOCKDKHQ9Idk0yIXZuX2E7kJ-xqkp3yI1";

    private static final String SECRET = "EBiljyc272gs-FzUDn6zsA9-rj8-rMOWi0ZAXHwKvOxCpy9ZEo1APyOVRl4F2SRnKJw2cD9HaG8nhCHM";

    private final static APIContext apiContext = new APIContext(CLIENT_ID, SECRET, "sandbox");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CauseService causeService;

    @Resource
    private PaypalService paypalService;

    @Resource
    private DonorService donorService;

    @Resource
    private CausePaymentService causePaymentService;

    @RequestMapping(path = "/paypal", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Paypal> addPaypal(@RequestBody Map<String, String> paymentDetails) {
        Paypal paypal = new Paypal();
        if (!paymentDetails.get("donorId").equals("")) {
            String oauthId = paymentDetails.get("donorId");
            paypal.setDonor(donorService.findByOauthId(oauthId));
        }

        paypal.setAmount(Double.parseDouble(paymentDetails.get("amount")));
        paypal.setTransactionId(paymentDetails.get("transactionId"));
        paypal.setCharityId(Long.parseLong(paymentDetails.get("charityId")));

        Paypal res = paypalService.addPaypal(paypal);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(path = "/paypal/causePayment/{causeId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CausePayment> addPaypalForCause(@PathVariable("causeId") long causeId, @RequestBody CausePayment causePayment) {
        CausePayment res = causePaymentService.addPayment(causePayment);

        float amountToDistribute;
        float amountToEach;
        Payment payment;

        Set<Charity> charitiesToPay;

        try {
            payment = findPaymentWithPaypalAPI(res.getTransactionId());
        } catch (PayPalRESTException e) {
            res.setTransactionId(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        amountToDistribute = Float.parseFloat(payment.getTransactions().get(0).getAmount().getTotal());

        charitiesToPay = causeService.findById(causeId).getCharities();

        //rounding to 2 decimal places
        amountToEach = (float) Math.round((amountToDistribute / charitiesToPay.size()) * 100) / 100;

        try {
            PayoutBatch payoutBatch = makePayout(charitiesToPay, amountToEach);
        } catch (PayPalRESTException e) {
            logger.error("ERROR: " + e.getMessage());
            res.setTransactionId(e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private Payment findPaymentWithPaypalAPI(String id) throws PayPalRESTException {
        return Payment.get(apiContext, id);
    }

    private PayoutBatch makePayout(Set<Charity> charitiesToPay, float amountEach) throws PayPalRESTException {
        Random random = new Random();
        Payout payout = new Payout();
        PayoutBatch batch;
        PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
        List<PayoutItem> items = new ArrayList<>();

        senderBatchHeader.setSenderBatchId(Double.toString(random.nextDouble()))
                .setEmailSubject("You have a donation for one of your causes");

        for (Charity charity : charitiesToPay) {
            Currency amount = new Currency();
            PayoutItem senderItem = new PayoutItem();

            amount.setValue(Float.toString(amountEach)).setCurrency("GBP");
            senderItem.setRecipientType("Email")
                    .setNote("Thanks for being part of Connect Charities")
                    .setReceiver(charity.getEmail())
                    .setSenderItemId(random12DigitCode(random))
                    .setAmount(amount);
            items.add(senderItem);
        }

        payout.setSenderBatchHeader(senderBatchHeader)
                .setItems(items);

        batch = payout.create(apiContext, new HashMap<String, String>());

        return batch;
    }

    private String random12DigitCode(Random random) {
        return Integer.toString((int) (random.nextFloat() * Math.pow(10, 12)));
    }

    @RequestMapping(path = "/paypal", method = RequestMethod.PATCH)
    @ResponseBody
    public void updatePaypal(@RequestBody Paypal paypal) {
        paypalService.updateSelective(paypal);
    }

    @RequestMapping(path = "/paypal/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deletePaypalById(@PathVariable("id") Long id) {
        paypalService.deleteById(id);
    }

    @RequestMapping(path = "/paypal/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Paypal> getPaypalById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(paypalService.findById(id), HttpStatus.OK);
    }

}
