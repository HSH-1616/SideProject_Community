package com.side.community.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class EmailConfig {

    private final ConfigVariable configVariable;

    public EmailConfig(ConfigVariable configVariable) {
        this.configVariable = configVariable;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(configVariable.getEmailUserName());
        mailSender.setPassword(configVariable.getEmailPassword());
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());
        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.starttls.required", true);
        properties.put("mail.smtp.connection-timeout", 5000);
        properties.put("mail.smtp.timeout", 5000);
        properties.put("mail.smtp.write timeout", 5000);
        properties.put("auth-code-expiration-millis", 1800000);
        return properties;
    }
}
