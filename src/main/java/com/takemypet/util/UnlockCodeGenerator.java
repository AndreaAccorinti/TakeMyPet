package com.takemypet.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Generates one-time unlock codes for blocked accounts.
 * Uses SecureRandom to avoid predictability.
 */
@Component
public class UnlockCodeGenerator {

    private static final String PREFIX = "TMP-";
    private static final int CODE_DIGITS = 5;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generate() {
        StringBuilder code = new StringBuilder(PREFIX);
        for (int i = 0; i < CODE_DIGITS; i++) {
            code.append(SECURE_RANDOM.nextInt(10));
        }
        return code.toString();
    }
}
