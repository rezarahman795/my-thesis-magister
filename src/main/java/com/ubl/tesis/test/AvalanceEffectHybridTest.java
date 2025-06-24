package com.ubl.tesis.test;

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

    public class AvalanceEffectHybridTest {

    private static String generateDynamicKey() {
        return "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    private static SecretKeySpec generateKey(String algorithm, String baseKey) {
        try {
            byte[] keyBytes = baseKey.getBytes();
            if (algorithm.equals("AES")) {
                keyBytes = padKey(keyBytes, 16); // Ensure AES key is 16 bytes
            } else if (algorithm.equals("Blowfish")) {
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
        String plaintext1 = "Hello World!";
        String plaintext2 = "Gello world!";

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
        String key = generateDynamicKey(); // Single key for both AES and Blowfish
        SecureRandom secureRandom = new SecureRandom();

        // Generate a random IV (16 bytes for AES)
        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Encrypt with AES
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec aesKeySpec = generateKey("AES", key);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivParameterSpec);
        byte[] aesEncrypted = aesCipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Encrypt AES ciphertext with Blowfish
        Cipher blowfishCipher = Cipher.getInstance("Blowfish");
        SecretKeySpec blowfishKeySpec = generateKey("Blowfish", key);
        blowfishCipher.init(Cipher.ENCRYPT_MODE, blowfishKeySpec);
        byte[] hybridEncrypted = blowfishCipher.doFinal(aesEncrypted);

        // Combine IV and Blowfish-encrypted ciphertext
        ByteBuffer byteBuffer = ByteBuffer.allocate(ivBytes.length + hybridEncrypted.length);
        byteBuffer.put(ivBytes); // Add IV to the beginning
        byteBuffer.put(hybridEncrypted);


        return  Base64.getEncoder().encodeToString(byteBuffer.array());
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

