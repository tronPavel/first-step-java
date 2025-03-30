package org.example.demo.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.demo.config.MailConfig;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailService {
    private static final String SMTP_HOST = MailConfig.get("mail.smtp.host");
    private static final int SMTP_PORT = Integer.parseInt(MailConfig.get("mail.smtp.port"));
    private static final String USERNAME = MailConfig.get("mail.smtp.username");
    private static final String PASSWORD = MailConfig.get("mail.smtp.password");

    public static void sendConfirmationEmail(String toEmail, String token) throws MessagingException, UnsupportedEncodingException {
        String subject = "Подтверждение регистрации";

        String link = "http://localhost:8080/confirm?token=" + token;
        String body = "Здравствуйте!\n" +
                "Перейдите по ссылке, чтобы подтвердить регистрацию: " + link;
        sendEmail(toEmail, subject, body);
    }

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME, "My Application"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
