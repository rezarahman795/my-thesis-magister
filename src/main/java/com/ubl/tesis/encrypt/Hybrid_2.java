package com.ubl.tesis.encrypt;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */
public class Hybrid_2 {


//    @PostMapping("/hybrid/encrypt/data-pelanggan-xyz")
//    public APIResponse<?> hybridEncrypt(@RequestBody String plaintext) throws Exception {
//        String key = generateDynamicKey(); // Single key for both AES and Blowfish
//        // Generate a random IV (16 bytes for AES)
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] ivBytes = new byte[16];
//        secureRandom.nextBytes(ivBytes);
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
//
//        // Encrypt with AES
//        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        SecretKeySpec aesKeySpec = generateKey("AES", key);
//        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivParameterSpec);
//        byte[] aesEncrypted = aesCipher.doFinal(plaintext.getBytes());
//
//        // Encrypt AES ciphertext with Blowfish
//        Cipher blowfishCipher = Cipher.getInstance("Blowfish");
//        SecretKeySpec blowfishKeySpec = generateKey("Blowfish", key);
//        blowfishCipher.init(Cipher.ENCRYPT_MODE, blowfishKeySpec);
//        byte[] hybridEncrypted = blowfishCipher.doFinal(aesEncrypted);
//
//        // Return only the encrypted data
//        return APIResponse.ok(Base64.getEncoder().encodeToString(hybridEncrypted));
//    }

//    @PostMapping("/hybrid/decrypt/data-pelanggan-xyz")
//    public ResponseEntity<String> hybridDecrypt(@RequestBody String ciphertext, @RequestParam String key) throws Exception {
//        SecureRandom secureRandom = new SecureRandom();
//        byte[] ivBytes = new byte[16];
//        secureRandom.nextBytes(ivBytes);
//        IvParameterSpec ivParameterSpec2 = new IvParameterSpec(ivBytes);
//
//
//        // Decrypt with Blowfish
//        Cipher blowfishCipher = Cipher.getInstance("Blowfish");
//        SecretKeySpec blowfishKeySpec = generateKey("Blowfish", key);
//        blowfishCipher.init(Cipher.DECRYPT_MODE, blowfishKeySpec);
//        byte[] blowfishDecrypted = blowfishCipher.doFinal(Base64.getDecoder().decode(ciphertext));
//
//        // Decrypt Blowfish plaintext with AES
//        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        SecretKeySpec aesKeySpec = generateKey("AES", key);
//        aesCipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ivParameterSpec2);
//        byte[] aesDecrypted = aesCipher.doFinal(blowfishDecrypted);
//
//
//        return ResponseEntity.ok(new String(aesDecrypted,StandardCharsets.UTF_8));
//    }
}
