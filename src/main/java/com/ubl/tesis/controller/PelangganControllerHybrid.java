package com.ubl.tesis.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ubl.tesis.request.RequestDataPelanggan;
import com.ubl.tesis.respone.APIResponse;
import com.ubl.tesis.service.PelangganService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


/**
 * @AUTHOR RR
 * @DATE 09/12/2024
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "informasi", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "HYBRID API")
@Service
public class PelangganControllerHybrid {

    private SecretKeySpec generateKey(String algorithm, String baseKey) {
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

    private byte[] padKey(byte[] keyBytes, int length) {
        byte[] paddedKey = new byte[length];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, length));
        return paddedKey;
    }


//    private final IvParameterSpec aesIv = new IvParameterSpec("rezaiv1234567890".getBytes()); // 16 bytes for AES IV

    private String generateDynamicKey() {
        return "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }


    @Autowired
    private PelangganService pelangganService;

    @PostMapping(value = "/hybrid/data-pelanggan-xyz")
    public APIResponse<?> getDataPelanggan(@RequestBody RequestDataPelanggan requestDataPelanggan) throws SQLException {
        try {
            return pelangganService.getDataPelanggan(requestDataPelanggan,false,false,false);
        } catch (Exception e) {
            return APIResponse.badRequest("Gagal");
        }
    }

    @PostMapping("/hybrid/encrypt/data-pelanggan-xyz")
    public APIResponse<?> hybridEncrypt(@RequestBody String plaintext) throws Exception {
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

        // Encode combined data as Base64
        String finalEncrypted = Base64.getEncoder().encodeToString(byteBuffer.array());

        return APIResponse.ok(finalEncrypted);
    }


    @PostMapping("/hybrid/decrypt/data-pelanggan-xyz")
    public ResponseEntity<String> hybridDecrypt(@RequestBody String ciphertext, @RequestParam String key) throws Exception {
        try {
            // Decode Base64 ciphertext
            ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(ciphertext));
            byte[] iv = new byte[16]; // Extract IV
            buffer.get(iv);
            byte[] actualCiphertext = new byte[buffer.remaining()];
            buffer.get(actualCiphertext);

            // Decrypt with Blowfish
            Cipher blowfishCipher = Cipher.getInstance("Blowfish");
            SecretKeySpec blowfishKeySpec = generateKey("Blowfish", key);
            blowfishCipher.init(Cipher.DECRYPT_MODE, blowfishKeySpec);
            byte[] blowfishDecrypted = blowfishCipher.doFinal(actualCiphertext);

            // Decrypt Blowfish plaintext with AES
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec aesKeySpec = generateKey("AES", key);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ivParameterSpec);
            byte[] aesDecrypted = aesCipher.doFinal(blowfishDecrypted);

            // Convert to string
            String decryptedText = new String(aesDecrypted, StandardCharsets.UTF_8);
            return ResponseEntity.ok(decryptedText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data Tidak Ditemukan ");
        }
    }

    @PostMapping(value = "data-pelanggan-xyz-hybrid")
    public APIResponse<?> getDataPelangganHybrid(@RequestBody RequestDataPelanggan requestDataPelanggan) throws SQLException {
        try {

            return pelangganService.getDataPelanggan(requestDataPelanggan,false,false,true);
        } catch (Exception e) {
            return APIResponse.badRequest("Gagal");
        }
    }

}
