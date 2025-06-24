package com.ubl.tesis.test;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */
import java.util.Arrays;
import java.util.Map;
import java.util.Base64;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Blowfish {

    private static final int NUM_ROUNDS = 16;
    private static final int BLOCK_SIZE = 8;

    // P-array dan S-boxes yang digunakan dalam algoritma Blowfish
    private static final int[] P = new int[18];
    private static final int[][] S = new int[4][256];

    // Inisialisasi P-array dan S-boxes dengan nilai standar
    static {
        // Nilai P-array dan S-boxes (dapat ditemukan dalam spesifikasi Blowfish)
        // Untuk tujuan contoh, ini hanya berisi nilai placeholder
        int[] pValues = new int[] {
                0x243F6A88, 0x85A308D3, 0x13198A2E, 0x03707344, 0xA4093822, 0x299F31D0,
                0x082EFA98, 0xEC4E6C89, 0x3B8F4A8E, 0xA2B21D5A, 0x96A3D1F8, 0x7C4B4DD0,
                0x73A02E85, 0x25E9B2F5, 0xDFE7D7D4, 0x9C9F87CB, 0xFAF60E0B, 0x2F466B3D
        };

        for (int i = 0; i < P.length; i++) {
            P[i] = pValues[i % pValues.length];
        }

        int[][] sValues = new int[][] {
                { 0xD1310BA6, 0x98DFB5AC, 0x2FFD72DB, 0xD01ADFB7, 0xB8D2C9E5, 0xE8D6E4B6 },  // S[0]
                { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 },               // S[1]
                { 0xFFFCFFFB, 0xFFFBFFFA, 0xFFFFFDFE, 0xFFFCFFFB, 0xD8BCFD63 },               // S[2]
                { 0x8B8F8B8F, 0x9A8E9A8C, 0xA89F8A8B, 0xBBA0BBAF, 0xB7A9BC50 }                // S[3]
        };

        for (int i = 0; i < S.length; i++) {
            S[i] = sValues[i];
        }
    }

    // Fungsi enkripsi Blowfish
    public static String BlowfishCryptography(Map<String, Object> data) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String plaintext = gson.toJson(data);

        // Membuat kunci yang dinamis berdasarkan tanggal
        String keyStr = "S3CR@P2T" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        byte[] keyBytes = keyStr.getBytes();

        // Menyesuaikan panjang kunci untuk 8 byte
        byte[] keyBytesFixed = new byte[8];
        System.arraycopy(keyBytes, 0, keyBytesFixed, 0, Math.min(keyBytes.length, keyBytesFixed.length));

        // Plaintext yang akan dienkripsi
        byte[] plaintextBytes = plaintext.getBytes();

        // Enkripsi blok menggunakan Blowfish
        byte[] encryptedBytes = encryptBlowfish(plaintextBytes, keyBytesFixed);

        // Mengembalikan hasil enkripsi dalam bentuk string Base64
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Fungsi enkripsi menggunakan algoritma Blowfish dengan S-boxes dan P-array
    private static byte[] encryptBlowfish(byte[] data, byte[] key) {
        int[] keys = prepareKey(key);

        byte[] output = new byte[data.length];

        for (int i = 0; i < data.length; i += BLOCK_SIZE) {
            byte[] block = new byte[BLOCK_SIZE];
            System.arraycopy(data, i, block, 0, BLOCK_SIZE);
            byte[] encryptedBlock = encryptBlock(block, keys);
            System.arraycopy(encryptedBlock, 0, output, i, BLOCK_SIZE);
        }

        return output;
    }

    // Persiapkan kunci untuk Blowfish
    private static int[] prepareKey(byte[] key) {
        int[] keys = new int[18];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = (key[i % key.length] << 24) | (key[(i + 1) % key.length] << 16) | (key[(i + 2) % key.length] << 8) | key[(i + 3) % key.length];
        }
        return keys;
    }

    // Enkripsi satu blok 8-byte menggunakan Blowfish
    private static byte[] encryptBlock(byte[] block, int[] keys) {
        int left = (block[0] << 24) | (block[1] << 16) | (block[2] << 8) | block[3];
        int right = (block[4] << 24) | (block[5] << 16) | (block[6] << 8) | block[7];

        left ^= keys[0];
        right ^= keys[1];

        for (int round = 1; round <= NUM_ROUNDS; round++) {
            int temp = left;
            left = right ^ f(left, keys, round);
            right = temp;
        }

        right ^= keys[NUM_ROUNDS + 2];
        left ^= keys[NUM_ROUNDS + 3];

        byte[] encryptedBlock = new byte[BLOCK_SIZE];
        encryptedBlock[0] = (byte) (left >>> 24);
        encryptedBlock[1] = (byte) (left >>> 16);
        encryptedBlock[2] = (byte) (left >>> 8);
        encryptedBlock[3] = (byte) left;
        encryptedBlock[4] = (byte) (right >>> 24);
        encryptedBlock[5] = (byte) (right >>> 16);
        encryptedBlock[6] = (byte) (right >>> 8);
        encryptedBlock[7] = (byte) right;

        return encryptedBlock;
    }

    // Fungsi f untuk enkripsi dalam Blowfish
    private static int f(int x, int[] keys, int round) {
        int a = (x >>> 24) & 0xFF;
        int b = (x >>> 16) & 0xFF;
        int c = (x >>> 8) & 0xFF;
        int d = x & 0xFF;

        return ((S[0][a] + S[1][b]) ^ S[2][c]) + S[3][d];
    }

    // Contoh penggunaan metode BlowfishCryptography
    public static void main(String[] args) {
        try {
            Map<String, Object> data = Map.of("name", "John Doe", "email", "johndoe@example.com");
            String encryptedData = BlowfishCryptography(data);
            System.out.println("Encrypted Data: " + encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

