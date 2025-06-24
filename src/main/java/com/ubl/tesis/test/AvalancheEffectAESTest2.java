package com.ubl.tesis.test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 21/12/2024
 */
public class AvalancheEffectAESTest2 {
    private static final int BLOCK_SIZE = 128;

    public static void main(String[] args) throws Exception {
        String input1 = "Hello World!";
        String input2 = "hello world!";

        byte[] key = generateKey();
        byte[] iv = generateIV();

        byte[] ciphertext1 = encryptAESCBC(input1.getBytes(), key, iv);
        byte[] ciphertext2 = encryptAESCBC(input2.getBytes(), key, iv);

        double avalancheEffect = calculateDifference(ciphertext1, ciphertext2);

        System.out.println("Input 1: " + input1);
        System.out.println("Ciphertext 1: " + Base64.getEncoder().encodeToString(ciphertext1));
        System.out.println("\nInput 2: " + input2);
        System.out.println("Ciphertext 2: " + Base64.getEncoder().encodeToString(ciphertext2));
        System.out.println("\nKey used: " + new String(key));
        System.out.println("Avalanche Effect: " + String.format("%.2f%%", avalancheEffect));
    }

    private static byte[] generateKey() {
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        return Arrays.copyOf(keyStr.getBytes(), 16);
    }

    private static byte[] encryptAESCBC(byte[] plaintext, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(plaintext);
    }

    private static double calculateDifference(byte[] original, byte[] modified) {
        int differentBits = 0;
        for (int i = 0; i < original.length; i++) {
            byte xor = (byte) (original[i] ^ modified[i]);
            differentBits += Integer.bitCount(xor & 0xFF);
        }

        int totalBits = original.length * 8;
        return (differentBits * 100.0) / totalBits;
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }


}
