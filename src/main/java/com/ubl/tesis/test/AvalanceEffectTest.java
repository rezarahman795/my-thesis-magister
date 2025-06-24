package com.ubl.tesis.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */

    public class AvalanceEffectTest {
    private static String generateDynamicKey() {
        return "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    private static SecretKeySpec generateKey(String algorithm, String baseKey) {
        try {
            byte[] keyBytes = baseKey.getBytes();
            if (algorithm.equals("AES")) {
                keyBytes = padKey(keyBytes, 16); // Ensure AES key is 16 bytes
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
    String plaintext1 = "Hello World!";
    String plaintext2 = "Gello World!";

    String encrypted1 = encrypt(plaintext1);
    String encrypted2 = encrypt(plaintext2);

    int perubahanBit = hitungPerubahanBit(encrypted1.getBytes(), encrypted2.getBytes());
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


        String keyStr = generateDynamicKey(); // Ukuran kunci 16 byte

        SecureRandom secureRandom = new SecureRandom();
        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKeySpec = generateKey("AES", keyStr);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        byte[] ivAndEncryptedBytes = new byte[ivBytes.length + encryptedBytes.length];
        System.arraycopy(ivBytes, 0, ivAndEncryptedBytes, 0, ivBytes.length);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, ivBytes.length, encryptedBytes.length);

        return Base64.getUrlEncoder().encodeToString(ivAndEncryptedBytes);
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

    public static double hitungPersentasePerubahan(int perubahanBit, int totalBit) {
        return ((double) perubahanBit / totalBit) * 100;
    }
}

