package com.side.community.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@RefreshScope
@Component
@Data
public class ConfigVariable {

    @Value("${DRIVERCLASSNAME}")
    private String driver;

    @Value("${URL}")
    private String url;

    @Value("${USERID}")
    private String userId;

    @Value("${PASSWORD}")
    private String password;
}
