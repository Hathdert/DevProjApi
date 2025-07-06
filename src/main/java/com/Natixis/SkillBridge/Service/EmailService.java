package com.Natixis.SkillBridge.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // private final JavaMailSender mailSender;

    // @Autowired
    // public EmailService(JavaMailSender mailSender) {
    //     this.mailSender = mailSender;
    // }

    // public void sendWelcomeEmail(String toEmail, String name) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(toEmail);
    //     message.setSubject("Welcome to SkillBridge!");
    //     message.setText("Olá " + name + ",\n\nObrigado por te registares na nossa plataforma.\n\nCumprimentos,\nEquipa SkillBridge");

    //     mailSender.send(message);
    // }

    
}
