/**
 * This class is used to generate Tokens for
 *
 * @name sessionKey
 * @length 64
 *
 * @name email verification
 * @length variable
 */

package com.example.backend.util;

public class Strings {
    private static final String characterPool = "123467890qwertyuiopasdfghjklzxcvbnm-_+$#!QWERTYUIOPASDFGHJKLZXCVBNM";

    public static String generateToken() {
        StringBuilder token = new StringBuilder();

        int tokenLength = 64;

        for (int i = 0; i < tokenLength; i++) {
            token.append(characterPool.charAt((int) (Math.random() * (characterPool.length()))));
        }

        return token.toString();
    }

    public static String generateToken(int amount) {
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            token.append((int) (Math.random() * (characterPool.length() + 1)));
        }

        return token.toString();
    }
}
