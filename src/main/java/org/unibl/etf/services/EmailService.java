package org.unibl.etf.services;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
     @Async
     void sendVerificationMail(String code,String to);
     @Async
     void sendApprovalInfo(String to);
}
