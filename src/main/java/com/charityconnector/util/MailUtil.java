package com.charityconnector.util;

import java.net.InetAddress;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;

public class MailUtil implements Runnable {
    private String email;// The email address of receiver
    private String code;// verify code
    private Long charityID;

    private int status = 0;

    public MailUtil(String email, String code,Long charityID) {
        this.email = email;
        this.code = code;
        this.charityID = charityID;
    }

    public void run() {

        // 1. create connection object javax.mail.Session
        // 2. create mail object javax.mail.Message
        // 3. send a active email
        String from = "398712463@qq.com";
        String host = "smtp.qq.com";

        Properties properties = System.getProperties();// Get system property
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", "sf");
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");

        try {
            //This part of code is needed for qq email.
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            // 1 Get a default session object
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("398712463@qq.com", "ofdxasfryentbhhd"); // Email accound and Authentication code
                }
            });

            // Change the verify IP to Heroku URL, so that every verify process would be happen in the Heroku Server
            String localIP  = InetAddress.getLocalHost().getHostAddress();
            String herokuURL = "http://connecting-charities-website.herokuapp.com/";
            // Get the IP address of the local machine
            // Create Email Object and set the email contents
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Account Verification");
            String content = "<html><head></head><body><h2>Please click the following link to verify your charity account:</h2></br></br><h3>" +
                    "<a href="+herokuURL+"verifyCharity/"+charityID+"/"+code +
                    ">"+herokuURL+"verifyCharity/"+charityID+"/"+code+"</href></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            Transport.send(message);
            System.out.println("The email is sent successfully to  "+email);
            status = 1;
        } catch (Exception e) {
            System.out.println("Error sending the email!");
            status=-1;
            e.printStackTrace();
        }
    }

    public int getStatus() {
        return status;
    }
}
