package com.ubl.tesis.decrypt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ubl.tesis.request.RequestDekrip;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @AUTHOR RR
 * @DATE 09/12/2024
 */
public class Blowfish_decrypt {

    public static String BlowfishDecryption(RequestDekrip dataReq) throws Exception {
        // Buat kunci dekripsi yang sama seperti pada metode enkripsi
        byte[] keyBytes = dataReq.getKey().getBytes();

        // Sesuaikan panjang kunci menjadi 8 byte
        byte[] keyBytesFixed = new byte[8];
        System.arraycopy(keyBytes, 0, keyBytesFixed, 0, Math.min(keyBytes.length, keyBytesFixed.length));

        // Decode Base64 dari teks terenkripsi
        byte[] encryptedBytes = Base64.getDecoder().decode(dataReq.getCipherText());

        // Buat SecretKeySpec dari kunci
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytesFixed, "Blowfish");

        // Inisialisasi Cipher untuk dekripsi
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        // Dekripsi teks
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Konversi byte ke string plaintext
        return new String(decryptedBytes);
    }


    public static String decryptBlowfishCBC(RequestDekrip dataReq) throws Exception {
        // Pastikan keyStr memiliki panjang tepat 8 karakter
        if (dataReq.getKey().length() > 8) {
            dataReq.setKey(dataReq.getKey().substring(0,8)); // Truncate to 16 bytes if necessary
        } else if (dataReq.getKey().length() < 8) {
            throw new IllegalArgumentException("Key must be exactly 16 characters long");
        }

        byte[] keyBytes = dataReq.getKey().getBytes(StandardCharsets.UTF_8);

        // Decode Base64 to byte array
        byte[] ivAndEncryptedBytesDecoded = Base64.getDecoder().decode(dataReq.getCipherText());

        // Extract IV and ciphertext
        byte[] ivBytes = new byte[8];
        byte[] encryptedBytes = new byte[ivAndEncryptedBytesDecoded.length - 8];
        System.arraycopy(ivAndEncryptedBytesDecoded, 0, ivBytes, 0, 8);
        System.arraycopy(ivAndEncryptedBytesDecoded, 8, encryptedBytes, 0, encryptedBytes.length);

        // Create IvParameterSpec with extracted IV
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // Create a SecretKeySpec from the key
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "Blowfish");

        // Initialize the Cipher for decryption
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


}
