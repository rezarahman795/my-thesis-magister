package com.ubl.tesis.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ubl.tesis.encrypt.Hybrid;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/*
  @AUTHOR RR
 * @DATE 11/12/2024
 */

import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

public class HybridDecryption {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String BLOWFISH_ALGORITHM = "Blowfish/CBC/PKCS5Padding";
    private static final String SALT = "staticSaltValue"; // Replace with a securely generated salt for production
    private static final int ITERATION_COUNT = 65536;
    private static final int AES_KEY_SIZE = 128; // in bits
    private static final int BLOWFISH_KEY_SIZE = 128; // in bits

    // Generate a random initialization vector for AES
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16]; // AES block size
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Generate IV for Blowfish
    public static IvParameterSpec generateBlowfishIv() {
        byte[] iv = new byte[8]; // Blowfish block size
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Generate secret keys for AES and Blowfish from a single password
    public static SecretKey generateKey(String password, String algorithm, int keySize) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, algorithm);
    }

    // AES Encryption
    public static String encryptAES(String input, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    // Blowfish Encryption (Updated with IV)
    public static String encryptBlowfish(String input, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(BLOWFISH_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }

    // AES Decryption
    public static String decryptAES(String cipherText, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText, StandardCharsets.UTF_8);
    }

    // Blowfish Decryption (Updated with IV)
    public static String decryptBlowfish(String cipherText, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(BLOWFISH_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText, StandardCharsets.UTF_8);
    }

    // Hybrid Encryption (Updated)
    public static String encryptWrap(String data, SecretKey aesKey, SecretKey blowfishKey, IvParameterSpec aesIv, IvParameterSpec blowfishIv) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);

        // Step 1: Encrypt using AES
        String aesEncrypted = encryptAES(plaintext, aesKey, aesIv);

        // Step 2: Encrypt AES result using Blowfish
        return encryptBlowfish(aesEncrypted, blowfishKey, blowfishIv);
    }

    // Hybrid Decryption (Updated)
    public static Map<String, Object> decryptWrap(String encryptedData, SecretKey aesKey, SecretKey blowfishKey, IvParameterSpec aesIv, IvParameterSpec blowfishIv) throws Exception {
        // Step 1: Decrypt using Blowfish
        String blowfishDecrypted = decryptBlowfish(encryptedData, blowfishKey, blowfishIv);

        // Step 2: Decrypt the result using AES
        String aesDecrypted = decryptAES(blowfishDecrypted, aesKey, aesIv);

        // Convert the JSON string back to a map
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        return gson.fromJson(aesDecrypted, type);
    }


    public static void main(String[] args) {
        try {
            // Key generation
            String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            keyStr = keyStr.substring(0, 16); // Ensure it is 16 bytes
            SecretKey aesKey = generateKey(keyStr, "AES", AES_KEY_SIZE);
            SecretKey blowfishKey = generateKey(keyStr, "Blowfish", BLOWFISH_KEY_SIZE);
            IvParameterSpec aesIv = generateIv();
            IvParameterSpec blowfishIv = generateBlowfishIv();

            // Sample data to encrypt
            Map<String, Object> data = new HashMap<>();
            data.put("cipherText", "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");
            // Encryption
            String encryptedData = encryptWrap("data", aesKey, blowfishKey, aesIv, blowfishIv);
            System.out.println("Encrypted Data: " + encryptedData);

            // Decryption
            Map<String, Object> decryptedData = decryptWrap(encryptedData, aesKey, blowfishKey, aesIv, blowfishIv);
            System.out.println("Decrypted Data: " + decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
}
}

