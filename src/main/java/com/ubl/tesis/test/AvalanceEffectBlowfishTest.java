package com.ubl.tesis.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */

    public class AvalanceEffectBlowfishTest {

    private static String generateDynamicKey() {
        return "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    private static SecretKeySpec generateKey(String algorithm, String baseKey) {
        try {
            byte[] keyBytes = baseKey.getBytes();
            if (algorithm.equals("Blowfish")) {
                keyBytes = padKey(keyBytes, 16); // Adjust length if necessary
            }
            return new SecretKeySpec(keyBytes, algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error generating key", e);
        }
    }

    private static byte[] padKey(byte[] keyBytes, int length) {
        byte[] paddedKey = new byte[length];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, length));
        return paddedKey;
    }

    public static void main(String[] args) throws Exception {
        String plaintext1 = "HelloWorld!";
        String plaintext2 = "HelloWorlc!";

        String encrypted1 = encrypt(plaintext1);
        String encrypted2 = encrypt(plaintext2);

        double perubahanBit = calculateDifference(encrypted1.getBytes(), encrypted2.getBytes());
        int totalBit = Math.max(encrypted1.getBytes().length, encrypted2.getBytes().length) * 8;
        double persentase = hitungPersentasePerubahan(perubahanBit, totalBit);

        System.out.println("Plaintext 1: " + plaintext1);
        System.out.println("Plaintext 2: " + plaintext2);
        System.out.println("Enkripsi 1: " + encrypted1);
        System.out.println("Enkripsi 2: " + encrypted2);
        System.out.println("Panjang Ciphertext (karakter) 1: "+encrypted1.length());
        System.out.println("Panjang Ciphertext (karakter) 2 : "+encrypted2.length());
        System.out.println("total bit : "+totalBit);
        System.out.println("Perubahan Bit: " + perubahanBit);
        System.out.println("Persentase Perubahan: " + persentase + "%");
    }


    public static String encrypt(String plaintext) throws Exception {

        String keyStr = generateDynamicKey();
        // Text to be encrypted
        byte[] plaintextBytes = plaintext.getBytes();

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = generateKey("Blowfish", keyStr);

        // Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Encrypt the plaintext
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // Print the encrypted text
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

//    public static int hitungPerubahanBit(byte[] hash1, byte[] hash2) {
//        int perubahanBit = 0;
//        int panjang = Math.min(hash1.length, hash2.length);
//        for (int i = 0; i < panjang; i++) {
//            int xor = hash1[i] ^ hash2[i];
//            perubahanBit += hitungBitSet(xor);
//        }
//        return perubahanBit;
//    }

    private static double calculateDifference(byte[] original, byte[] modified) {
        int differentBits = 0;
        for (int i = 0; i < original.length; i++) {
            byte xor = (byte) (original[i] ^ modified[i]);
            differentBits += Integer.bitCount(xor & 0xFF);
        }
        return (differentBits * 100.0) / (original.length * 8);
    }

    public static int hitungBitSet(int nilai) {
        int count = 0;
        while (nilai > 0) {
            count += nilai & 1;
            nilai >>= 1;
        }
        return count;
    }

    public static double hitungPersentasePerubahan(double perubahanBit, double totalBit) {
        return ((double) perubahanBit / totalBit) * 100;
    }
}

