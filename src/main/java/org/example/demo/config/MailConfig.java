package org.example.demo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = MailConfig.class.getClassLoader().getResourceAsStream("mail.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден mail.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
