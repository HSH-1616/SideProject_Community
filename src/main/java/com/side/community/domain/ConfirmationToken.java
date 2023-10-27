package com.side.community.domain;

import java.security.SecureRandom;
import java.util.Base64;
import lombok.Getter;

@Getter
public class ConfirmationToken {
    public ConfirmationToken() {
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    private String token;

    public static ConfirmationToken confirmationToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.token = generateToken();
        return confirmationToken;
    }

    //token 생성
    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        return token;
    }
}
