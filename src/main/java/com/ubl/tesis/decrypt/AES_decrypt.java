package com.ubl.tesis.decrypt;

import com.ubl.tesis.request.RequestDekrip;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 08/12/2024
 */
@Service
public class AES_decrypt {
    public static String decryptAES128bit(String encryptedText) throws Exception {
        // Ensure the key is exactly 16 bytes (128 bits)
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            keyStr = keyStr.substring(0, 16); // Truncate to 16 bytes if necessary
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);

        // Decode Base64 to byte array
        byte[] ivAndEncryptedBytesDecoded = Base64.getDecoder().decode(encryptedText);

        // Extract IV and ciphertext
        byte[] ivBytes = new byte[16];
        byte[] encryptedBytes = new byte[ivAndEncryptedBytesDecoded.length - 16];
        System.arraycopy(ivAndEncryptedBytesDecoded, 0, ivBytes, 0, 16);
        System.arraycopy(ivAndEncryptedBytesDecoded, 16, encryptedBytes, 0, encryptedBytes.length);

        // Create IvParameterSpec with extracted IV
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the Cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    public static String decryptAES128bit(RequestDekrip req) throws Exception {
        // Pastikan keyStr memiliki panjang tepat 16 karakter
        if (req.getKey().length() > 16) {
            req.setKey(req.getKey().substring(0, 16)); // Truncate to 16 bytes if necessary
        } else if (req.getKey().length() < 16) {
            throw new IllegalArgumentException("Key must be exactly 16 characters long");
        }

        byte[] keyBytes = req.getKey().getBytes(StandardCharsets.UTF_8);

        // Decode Base64 to byte array
        byte[] ivAndEncryptedBytesDecoded = Base64.getDecoder().decode(req.getCipherText());

        // Extract IV and ciphertext
        byte[] ivBytes = new byte[16];
        byte[] encryptedBytes = new byte[ivAndEncryptedBytesDecoded.length - 16];
        System.arraycopy(ivAndEncryptedBytesDecoded, 0, ivBytes, 0, 16);
        System.arraycopy(ivAndEncryptedBytesDecoded, 16, encryptedBytes, 0, encryptedBytes.length);

        // Create IvParameterSpec with extracted IV
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Initialize the Cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        long endTime=System.nanoTime();
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }



}
