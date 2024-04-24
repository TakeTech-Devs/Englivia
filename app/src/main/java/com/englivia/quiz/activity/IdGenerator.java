package com.englivia.quiz.activity;

import java.security.SecureRandom;

public class IdGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 32;

    public static String generateRandomId() {
        StringBuilder id = new StringBuilder(ID_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            id.append(CHARACTERS.charAt(randomIndex));
        }

        return id.toString();
    }

    public static void main(String[] args) {
        String generatedId = generateRandomId();
        System.out.println("Generated ID: " + generatedId);
    }
}
