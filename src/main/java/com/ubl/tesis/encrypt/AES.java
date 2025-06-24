package com.ubl.tesis.encrypt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR RR
 * @DATE 08/12/2024
 */
public class AES {

    public static String encryptAES128(Map data) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);
        long startTime=System.nanoTime();
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
        long endTime=System.nanoTime();
        long duration = endTime - startTime; // dalam nanodetik
        System.out.println("Waktu Eksekusi: " + duration + " nanodetik");
        // Encode as Base64
        return Base64.getEncoder().encodeToString(ivAndEncryptedBytes);
    }

    public static String encryptAES128bit(Map data) throws Exception {
        Map map = new HashMap();
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        LocalDate localDateNow = LocalDate.now();
        String plaintext = gson.toJson(data);

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

        // Print the encrypted text
        String encryptedText = Base64.getEncoder().encodeToString(ivAndEncryptedBytes);
        System.out.println("Encrypted text: " + encryptedText);

        // Dekripsi
        // Decode Base64 ke byte array
        byte[] ivAndEncryptedBytesDecoded = Base64.getDecoder().decode(encryptedText);

        // Ekstrak IV dan ciphertext
        byte[] ivBytesExtracted = new byte[16];
        byte[] encryptedBytesExtracted = new byte[ivAndEncryptedBytesDecoded.length - 16];
        System.arraycopy(ivAndEncryptedBytesDecoded, 0, ivBytesExtracted, 0, 16);
        System.arraycopy(ivAndEncryptedBytesDecoded, 16, encryptedBytesExtracted, 0, encryptedBytesExtracted.length);

        // Membuat IvParameterSpec dengan IV yang diekstrak
        IvParameterSpec ivParameterSpecExtracted = new IvParameterSpec(ivBytesExtracted);

        // Menginisialisasi Cipher untuk dekripsi
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpecExtracted);

        // Mendekripsi ciphertext
        byte[] decryptedBytes = cipherDecrypt.doFinal(encryptedBytesExtracted);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);

        // Menampilkan hasil dekripsi
        System.out.println("Decrypted text: " + decryptedText);

        return encryptedText;
    }
}
