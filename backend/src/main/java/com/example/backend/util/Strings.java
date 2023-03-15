package com.example.backend.util;

import java.security.SecureRandom;
import java.util.Base64;

public class Strings {
    private static final SecureRandom rand = new SecureRandom();

    public static String generateToken() {
        byte[] b = new byte[64];
        rand.nextBytes(b);
        return new String(Base64.getEncoder().encode(b));
    }

    public static String generateToken(int amount) {
        byte[] b = new byte[amount];
        rand.nextBytes(b);
        return new String(Base64.getEncoder().encode(b));
    }
}
