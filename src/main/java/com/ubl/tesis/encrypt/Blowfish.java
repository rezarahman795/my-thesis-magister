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
public class Blowfish {

    public static String BlowfishCryptography(Map data) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);
        long startTime=System.nanoTime();
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        byte[] keyBytes = keyStr.getBytes();

        // Adjust the length of the key to match Blowfish key size requirements
        byte[] keyBytesFixed = new byte[8];
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
        long endTime=System.nanoTime();
        long duration = endTime - startTime; // dalam nanodetik
        System.out.println("Waktu Eksekusi: " + duration + " nanodetik");
        // Print the encrypted text
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    public static String BlowfishCryptographyCBC(Map data) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);

        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        keyStr = keyStr.substring(0, 8); // Truncate to 16 bytes if necessary
        byte[] keyBytes = keyStr.getBytes(StandardCharsets.UTF_8);

        // Generate a random IV (8 bytes for blowfish)
        SecureRandom secureRandom = new SecureRandom();
        byte[] ivBytes = new byte[8];
        secureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Text to be encrypted
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "Blowfish");

        // Initialize the Cipher for encryption
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,ivParameterSpec);

        // Encrypt the plaintext
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        // Combine IV and encrypted text
        byte[] ivAndEncryptedBytes = new byte[ivBytes.length + encryptedBytes.length];
        System.arraycopy(ivBytes, 0, ivAndEncryptedBytes, 0, ivBytes.length);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, ivBytes.length, encryptedBytes.length);


        // Print the encrypted text
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
