package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.unibl.etf.services.EmailService;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendInfoMail(String mail,String subject, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(mail);
            this.mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Async
    public void sendVerificationMail(String code,String to){
        var str="Online forum\n"+"Your login verification code:"+code;
        this.sendInfoMail(str,"Forum verification code",to);
    }

    @Override
    @Async
    public void sendApprovalInfo(String to) {
        this.sendInfoMail("Your online forum account has been successfully approved. Enjoy using the forum.","Online forum account approved",to);
    }
}
