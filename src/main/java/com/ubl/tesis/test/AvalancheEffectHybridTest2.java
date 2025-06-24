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
 * @DATE 31/12/2024
 */
public class AvalancheEffectHybridTest2 {
    public static void main(String[] args) throws Exception {
        String input1 = "Hello World!";
        String input2 = "hello world!";

        // Generate keys
        byte[] aesKey = generateAESKey();
        byte[] blowfishKey = generateBlowfishKey();
        byte[] iv = generateIV();

        // Process first input
        byte[] aesResult1 = encryptAESCBC(input1.getBytes(), aesKey, iv);
        byte[] finalResult1 = encryptBlowfish(aesResult1, blowfishKey);

        // Process second input
        byte[] aesResult2 = encryptAESCBC(input2.getBytes(), aesKey, iv);
        byte[] finalResult2 = encryptBlowfish(aesResult2, blowfishKey);

        // Calculate avalanche effect
        double avalancheEffect = calculateDifference(finalResult1, finalResult2);
        int perubahanBit = hitungPerubahanBit(finalResult1,finalResult2);
        int totalBit = Math.max(finalResult1.length, finalResult1.length) * 8;
        // Display results
        System.out.println("perubahan bit : "+perubahanBit);
        System.out.println("totalBit : "+totalBit);
        System.out.println("Input 1: " + input1);
        System.out.println("AES Output 1: " + Base64.getEncoder().encodeToString(aesResult1));
        System.out.println("Final Output 1: " + Base64.getEncoder().encodeToString(finalResult1));

        System.out.println("\nInput 2: " + input2);
        System.out.println("AES Output 2: " + Base64.getEncoder().encodeToString(aesResult2));
        System.out.println("Final Output 2: " + Base64.getEncoder().encodeToString(finalResult2));

        System.out.println("\nAES Key: " + new String(aesKey));
        System.out.println("Blowfish Key: " + new String(blowfishKey));
        System.out.println("Final Avalanche Effect: " + String.format("%.2f%%", avalancheEffect));
    }

    private static byte[] generateAESKey() {
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        return Arrays.copyOf(keyStr.getBytes(), 16);
    }

    private static byte[] generateBlowfishKey() {
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

    private static byte[] encryptBlowfish(byte[] plaintext, byte[] key) throws Exception {
        Cipher aesCipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "Blowfish");
        aesCipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return aesCipher.doFinal(plaintext);
    }

    private static byte[] encryptHybrid(byte[] plaintext, byte[] key,byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(plaintext);
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static double calculateDifference(byte[] original, byte[] modified) {
        int differentBits = 0;
        for (int i = 0; i < original.length; i++) {
            byte xor = (byte) (original[i] ^ modified[i]);
            differentBits += Integer.bitCount(xor & 0xFF);
        }
        return (differentBits * 100.0) / (original.length * 8);
    }

    public static int hitungPerubahanBit(byte[] hash1, byte[] hash2) {
        int perubahanBit = 0;
        int panjang = Math.min(hash1.length, hash2.length);
        for (int i = 0; i < panjang; i++) {
            int xor = hash1[i] ^ hash2[i];
            perubahanBit += hitungBitSet(xor);
        }
        return perubahanBit;
    }

    public static int hitungBitSet(int nilai) {
        int count = 0;
        while (nilai > 0) {
            count += nilai & 1;
            nilai >>= 1;
        }
        return count;
    }
}
