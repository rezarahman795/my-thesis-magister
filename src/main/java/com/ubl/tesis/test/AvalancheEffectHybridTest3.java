package com.ubl.tesis.test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

/**
 * @AUTHOR RR
 * @DATE 01/01/2025
 */
public class AvalancheEffectHybridTest3 {
    public static class EncryptionResult {
        public final byte[] ciphertext;
        public final byte[] aesKey;
        public final byte[] blowfishKey;
        public final byte[] iv;
        public final int totalBits;
        public final int changedBits;

        public EncryptionResult(byte[] ciphertext, byte[] aesKey, byte[] blowfishKey, byte[] iv, int totalBits, int changedBits) {
            this.ciphertext = ciphertext;
            this.aesKey = aesKey;
            this.blowfishKey = blowfishKey;
            this.iv = iv;
            this.totalBits = totalBits;
            this.changedBits = changedBits;
        }
    }

    public static EncryptionResult encryptHybrid(String input) throws Exception {
        byte[] aesKey = generateKey();
        byte[] blowfishKey = generateKey();
        byte[] iv = generateIV();
        int inputBits = input.getBytes().length * 8;

        // AES encryption
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivSpec);
        byte[] aesOutput = aesCipher.doFinal(input.getBytes());
        int aesChangedBits = calculateChangedBits(input.getBytes(), aesOutput);

        // Blowfish encryption
        Cipher blowfishCipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        SecretKeySpec blowfishKeySpec = new SecretKeySpec(blowfishKey, "Blowfish");
        blowfishCipher.init(Cipher.ENCRYPT_MODE, blowfishKeySpec);
        byte[] finalOutput = blowfishCipher.doFinal(aesOutput);
        int totalChangedBits = calculateChangedBits(input.getBytes(), finalOutput);

        return new EncryptionResult(finalOutput, aesKey, blowfishKey, iv, inputBits, totalChangedBits);
    }

    private static byte[] generateKey() {
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        return Arrays.copyOf(keyStr.getBytes(), 16);
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static int calculateChangedBits(byte[] original, byte[] modified) {
        int minLength = Math.min(original.length, modified.length);
        int changedBits = 0;
        for (int i = 0; i < minLength; i++) {
            byte xor = (byte) (original[i] ^ modified[i]);
            changedBits += Integer.bitCount(xor & 0xFF);
        }
        return changedBits;
    }

    public static double calculateAvalanche(byte[] arr1, byte[] arr2) {
        int differentBits = 0;
        for (int i = 0; i < arr1.length; i++) {
            byte xor = (byte) (arr1[i] ^ arr2[i]);
            differentBits += Integer.bitCount(xor & 0xFF);
        }
        return (differentBits * 100.0) / (arr1.length * 8);
    }

    public static void main(String[] args) throws Exception {
        String input1 = "HelloWorld";
        String input2 = "HelloWorlc";

        EncryptionResult result1 = encryptHybrid(input1);
        EncryptionResult result2 = encryptHybrid(input2);

        double avalancheEffect = calculateAvalanche(result1.ciphertext, result2.ciphertext);

        System.out.println("Input 1: " + input1);
        System.out.println("Output 1: " + Base64.getEncoder().encodeToString(result1.ciphertext));
        System.out.println("Total Bits: " + result1.totalBits);
        System.out.println("Changed Bits: " + result1.changedBits);

        System.out.println("\nInput 2: " + input2);
        System.out.println("Output 2: " + Base64.getEncoder().encodeToString(result2.ciphertext));
        System.out.println("Total Bits: " + result2.totalBits);
        System.out.println("Changed Bits: " + result2.changedBits);

        System.out.println("\nAvalanche Effect: " + String.format("%.2f%%", avalancheEffect));
    }
}
