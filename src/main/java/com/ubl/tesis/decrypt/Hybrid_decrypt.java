package com.ubl.tesis.decrypt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ubl.tesis.request.RequestDekrip;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @AUTHOR RR
 * @DATE 10/12/2024
 */
public class Hybrid_decrypt {
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String BLOWFISH_ALGORITHM = "Blowfish";
    private static final String SALT = "staticSaltValue";
    private static final int ITERATION_COUNT = 65536;
    // Generate a random initialization vector for AES
    public static IvParameterSpec generateIv_decrypt() {
        byte[] iv = new byte[16]; // AES block size
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Generate IV for Blowfish
    public static IvParameterSpec generateBlowfishIv_decrypt() {
        byte[] iv = new byte[8]; // Blowfish block size
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // Generate secret keys for AES and Blowfish from a single password
    public static SecretKey generateKey_decrypt(String password, String algorithm, int keySize) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, algorithm);
    }

    // AES Decryption
    public static String decryptAES(String cipherText, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText, StandardCharsets.UTF_8);
    }

    // Blowfish Decryption (Updated with IV)
    public static String decryptBlowfish(RequestDekrip dataReq, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(BLOWFISH_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(dataReq.getCipherText()));
        return new String(plainText, StandardCharsets.UTF_8);
    }

    // Hybrid Decryption (Updated)
    public static String decryptWrap(RequestDekrip encryptedData) throws Exception {
        // Validasi input
        if (encryptedData == null || encryptedData.getCipherText() == null || encryptedData.getKey() == null) {
            throw new IllegalArgumentException("Invalid input: cipherText or key is null.");
        }

        // Step 1: Decrypt using Blowfish
        String blowfishDecryptedText = BlowfishDecryption(encryptedData);

        // Step 2: Decrypt the result using AES
        RequestDekrip aesRequest = new RequestDekrip();
        aesRequest.setCipherText(blowfishDecryptedText);
        aesRequest.setKey(encryptedData.getKey());
        return decryptAES128bit(aesRequest);
    }

    public static String BlowfishDecryption(RequestDekrip dataReq) throws Exception {
        // Buat kunci dekripsi yang sama seperti pada metode enkripsi
        byte[] keyBytes = dataReq.getKey().getBytes();

        // Sesuaikan panjang kunci menjadi 16 byte
        byte[] keyBytesFixed = new byte[16];
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
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
