package com.jamia.jamiaakbira.service;

public interface EmailService {
    void sendNewAccountEmail(String name,String to,String key);
    void sendPasswordResetEmail(String name,String to,String key);
}
