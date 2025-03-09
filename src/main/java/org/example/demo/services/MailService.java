package org.example.demo.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.demo.config.MailConfig;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class MailService {
    // Допустим, хотим отправлять с Gmail
    // Включите "less secure apps" (или используйте OAuth2/App Password),
    // иначе Gmail может заблокировать авторизацию
    private static final String SMTP_HOST = MailConfig.get("mail.smtp.host");
    private static final int SMTP_PORT = Integer.parseInt(MailConfig.get("mail.smtp.port"));
    private static final String USERNAME = MailConfig.get("mail.smtp.username");
    private static final String PASSWORD = MailConfig.get("mail.smtp.password"); // пароль или App Password

    public static void sendConfirmationEmail(String toEmail, String token) throws MessagingException, UnsupportedEncodingException {
        // Тема письма
        String subject = "Подтверждение регистрации";

        // Текст письма
        String link = "http://localhost:8080/confirm?token=" + token;
        String body = "Здравствуйте!\n" +
                "Перейдите по ссылке, чтобы подтвердить регистрацию: " + link;

        // Вызываем общий метод отправки
        sendEmail(toEmail, subject, body);
    }

    // Общий метод для отправки письма
    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        // 1. Настраиваем свойства
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");              // Требуется авторизация
        props.put("mail.smtp.starttls.enable", "true");   // TLS
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));

        // 2. Создаём сессию с аутентификацией
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Возвращаем логин/пароль
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        // 3. Формируем письмо
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME, "My Application"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);

        // 4. Отправляем
        Transport.send(message);
    }
}
