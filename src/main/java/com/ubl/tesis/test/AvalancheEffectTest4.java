package com.ubl.tesis.test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @AUTHOR RR
 * @DATE 31/12/2024
 */
public class AvalancheEffectTest4 {
    public static void main(String[] args) throws Exception {
        String input1 = "HelloWorld";
        String input2 = "HelloWorlc";

        byte[] key = generateRandomKey();

        byte[] ciphertext1 = encryptBlowfish(input1.getBytes(), key);
        System.out.println("ciphertext 1 :"+ Arrays.toString(ciphertext1));
        byte[] ciphertext2 = encryptBlowfish(input2.getBytes(), key);
        System.out.println("ciphertext 2 :"+ Arrays.toString(ciphertext2));

        double avalancheEffect = calculateDifference(ciphertext1, ciphertext2);

        System.out.println("Input 1: " + input1);
        System.out.println("Input 2: " + input2);
        System.out.println("Avalanche Effect: " + String.format("%.2f%%", avalancheEffect));
    }

    private static byte[] encryptBlowfish(byte[] plaintext, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(plaintext);
    }

    private static double calculateDifference(byte[] original, byte[] modified) {
        int differentBits = 0;
        for (int i = 0; i < original.length; i++) {
            byte xor = (byte) (original[i] ^ modified[i]);
            differentBits += Integer.bitCount(xor & 0xFF);
        }
        return (differentBits * 100.0) / (original.length * 8);
    }

    private static byte[] generateRandomKey() {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        return key;
    }
}
