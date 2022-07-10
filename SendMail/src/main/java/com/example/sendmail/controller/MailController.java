package com.example.sendmail.controller;

import com.example.sendmail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class MailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailService mailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/send")
    public String showForm() {
        return "mail/send_mail.html";
    }

    @PostMapping("/send")
    public String send(@RequestParam("to") String to,
                       @RequestParam("subject") String subject,
                       @RequestParam("content") String body,
                       @RequestParam(name = "filename", required = false) MultipartFile multipartFile) {
//            HttpServletRequest request ,
//            @RequestParam(name = "filename" , required = false) MultipartFile multipartFile) {
//        //Thay vi truyền parameter mình có thể sử dụng HttpServletRequest
//        String to = request.getParameter("to") ;
//        String subject = request.getParameter("subject");
//        String body = request.getParameter("content");
//
        try {
            //mimemessage : gửi mail
            //mimemessagehepper : xét thông tin gửi đi
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name()); // send file html
//             MimeMessageHelper helper = new MimeMessageHelper(message,true);
//            Khi gửi ảnh , nên để multipart trong MimeMessageHelper là true
            Context context = new Context();
            context.setVariable("data", body);
            String html = templateEngine.process("test.html", context);

//            String mailContent = "<hr><img src='cid:logoImage'/>";
            helper.setTo(to);
//            helper.setText(body, true);
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setFrom("doanducminh11082002@gmail.com");

            //Khi gửi ảnh sử ClassPathResource
            ClassPathResource resource = new ClassPathResource("/static/image/logo.png");
            helper.addInline("logoImage" , resource);

            //Gửi tệp dữ liệu
            if (!multipartFile.isEmpty()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                InputStreamSource source = new InputStreamSource() {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return multipartFile.getInputStream();
                    }
                };
                helper.addAttachment(filename, source);
            }
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "mail/check.html";
    }

    //send mail thông qua mail service
    @GetMapping("/send_mail/service")
    public String sendMail() {
        //Ta có thể sử dụng luồng để tránh chờ đợi gửi email ở phía gmail
        new Thread() {
            @Override
            public void run() {
                mailService.sendEmail("doanducminh11082002@gmail.com", "test", "test noi dung gmail nha");
            }
        }.start();
        return "mail/check.html";
    }
}
