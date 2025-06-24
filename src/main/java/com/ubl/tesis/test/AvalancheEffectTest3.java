package com.ubl.tesis.test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 23/12/2024
 */
public class AvalancheEffectTest3 {
    public static void main(String[] args) throws Exception {
        // Test cases with single bit differences
        String input1 = "HelloWorld";
        String input2 = "HelloWorlc"; // Single character change

        System.out.println("Testing Avalanche Effect with inputs:");
        testAvalancheEffect(input1, input2);
    }

    public static void testAvalancheEffect(String input1, String input2) throws Exception {
        // Get encrypted values
        String encrypted1 = encrypt(input1);
        String encrypted2 = encrypt(input2);

        // Convert Base64 to binary
        byte[] binary1 = Base64.getDecoder().decode(encrypted1);
        byte[] binary2 = Base64.getDecoder().decode(encrypted2);

        // Skip IV (first 16 bytes) when comparing
        byte[] ciphertext1 = Arrays.copyOfRange(binary1, 16, binary1.length);
        byte[] ciphertext2 = Arrays.copyOfRange(binary2, 16, binary2.length);

        // Calculate differences
        int differentBits = countDifferentBits(ciphertext1, ciphertext2);
        int totalBits = ciphertext1.length * 8;
        double avalancheEffect = (differentBits * 100.0) / totalBits;

        // Print results
        System.out.println("Input 1: " + input1);
        System.out.println("Input 2: " + input2);
        System.out.println("Different bits in ciphertext: " + differentBits);
        System.out.println("Total bits compared: " + totalBits);
        System.out.println("Avalanche Effect: " + String.format("%.2f%%", avalancheEffect));
    }

    private static int countDifferentBits(byte[] array1, byte[] array2) {
        int count = 0;
        for (int i = 0; i < array1.length; i++) {
            byte xor = (byte) (array1[i] ^ array2[i]);
            count += Integer.bitCount(xor & 0xFF);
        }
        return count;
    }

    // Your existing encryption methods here
    private static String generateDynamicKey() {
        return "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    private static SecretKeySpec generateKey(String algorithm, String baseKey) {
        try {
            byte[] keyBytes = baseKey.getBytes();
            if (algorithm.equals("AES")) {
                keyBytes = padKey(keyBytes, 16);
            } else if (algorithm.equals("Blowfish")) {
                keyBytes = padKey(keyBytes, 16);
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

    public static String encrypt(String plaintext) throws Exception {
        String key = generateDynamicKey();
        SecureRandom secureRandom = new SecureRandom();

        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec aesKeySpec = generateKey("AES", key);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivParameterSpec);
        byte[] aesEncrypted = aesCipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        Cipher blowfishCipher = Cipher.getInstance("Blowfish");
        SecretKeySpec blowfishKeySpec = generateKey("Blowfish", key);
        blowfishCipher.init(Cipher.ENCRYPT_MODE, blowfishKeySpec);
        byte[] hybridEncrypted = blowfishCipher.doFinal(aesEncrypted);

        ByteBuffer byteBuffer = ByteBuffer.allocate(ivBytes.length + hybridEncrypted.length);
        byteBuffer.put(ivBytes);
        byteBuffer.put(hybridEncrypted);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }
}
