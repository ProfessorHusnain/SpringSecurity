package com.jamia.jamiaakbira.service.Impl;

import com.jamia.jamiaakbira.exception.NotificationException;
import com.jamia.jamiaakbira.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.jamia.jamiaakbira.utils.EmailUtils.getMailMessage;
import static com.jamia.jamiaakbira.utils.EmailUtils.getRestPasswordMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification.";
    private static final String PASSWORD_RESET_REQUEST = "Password Rest Request";
    private final JavaMailSender sender;
    @Value("${spring.mail.verification.host}")
    private String host;
    @Value("${spring.mail.username}")
    private  String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String to, String key) {
        try {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getMailMessage(name,host,key));
            sender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new NotificationException("Unable to send the email");
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String key) {
        try {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getRestPasswordMessage(name,host,key));
            sender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new NotificationException("Unable to send the email");
        }
    }
}
