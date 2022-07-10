package com.example.sendmail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    @Autowired
    SpringTemplateEngine templateEngine;//gui file html

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {


            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            //load template email with content
            Context context = new Context();
            context.setVariable("data", body);
            String html = templateEngine.process("hi.html", context);

            ///send mail
            helper.setTo(to);
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setFrom("trailangdong11082002@gmail.com");
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
