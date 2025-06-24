package com.ubl.tesis.encrypt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @AUTHOR RR
 * @DATE 08/12/2024
 */
public class Hybrid {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String BLOWFISH_ALGORITHM = "Blowfish/CBC/PKCS5Padding";
    private static final String SALT = "staticSaltValue"; // Replace with a securely generated salt for production
    private static final int ITERATION_COUNT = 65536;


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

    // Hybrid Encryption (Updated)
    public static String encryptWrap(String data2) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data2);

        // Step 1: Encrypt using AES
        String aesEncrypted = encryptAES128(plaintext);

        // Step 2: Encrypt AES result using Blowfish
        return BlowfishCryptography(aesEncrypted);
    }

    public static String encryptAES128(String data3) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data3);

        // Ensure the key is exactly 16 bytes (128 bits)
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        keyStr = keyStr.substring(0, 16); // Truncate to 16 bytes if necessary
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);

        // Generate a random IV (16 bytes for AES)
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Text to be encrypted
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Encrypt the plaintext
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // Combine IV and encrypted text
        byte[] ivAndEncryptedBytes = new byte[ivBytes.length + encryptedBytes.length];
        System.arraycopy(ivBytes, 0, ivAndEncryptedBytes, 0, ivBytes.length);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, ivBytes.length, encryptedBytes.length);

        // Encode as Base64
        return Base64.getEncoder().encodeToString(ivAndEncryptedBytes);
    }

    public static String BlowfishCryptography(String data) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);

        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        byte[] keyBytes = keyStr.getBytes();

        // Adjust the length of the key to match Blowfish key size requirements
        byte[] keyBytesFixed = new byte[16];
        System.arraycopy(keyBytes, 0, keyBytesFixed, 0, Math.min(keyBytes.length, keyBytesFixed.length));

        // Text to be encrypted
        byte[] plaintextBytes = plaintext.getBytes();

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytesFixed, "Blowfish");

        // Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // Encrypt the plaintext
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // Print the encrypted text
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
