package com.side.community.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@RefreshScope
@Component
@Data
public class ConfigVariable {
    //DataSource
    @Value("${DRIVERCLASSNAME}")
    private String driver;

    @Value("${URL}")
    private String url;

    @Value("${USERID}")
    private String userId;

    @Value("${PASSWORD}")
    private String password;

    //Email
    @Value("${EMAIL_USERNAME}")
    private String emailUserName;

    @Value("${EMAIL_PASSWORD}")
    private String emailPassword;

    //Redis
    @Value("${REDIS_HOST}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private int redisPort;

    @Value("${REDIS_PASSWORD}")
    private String redisPassword;

    //Recaptcha
    @Value("${RECAPTCHA_KEY}")
    private String recaptchaKey;
}
