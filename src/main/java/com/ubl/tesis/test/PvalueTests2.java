package com.ubl.tesis.test;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */
public class PvalueTests2 {
    public static void main(String[] args) {
        int degreesOfFreedom = 255; // 256 nilai byte, df = 256 - 1
        double pValue = calculatePValue(55.213114754098385, degreesOfFreedom);
        System.out.printf("P-Value: %.4f%n", pValue);

        // Interpretasi hasil
        if (pValue > 0.05) {
            System.out.println("Distribusi byte ciphertext seragam (H0 diterima).");
        } else {
            System.out.println("Distribusi byte ciphertext tidak seragam (H0 ditolak).");
        }
    }

    public static double calculatePValue(double chiSquare, int degreesOfFreedom) {
        // Menghitung p-value menggunakan distribusi Chi-Square
        // P-Value dihitung dengan Complementary Error Function
        double k = (double) degreesOfFreedom / 2;
        double x = chiSquare / 2;

        // Menggunakan fungsi gamma untuk approximasi p-value
        return 1.0 - gammaCdf(x, k);
    }

    public static double gammaCdf(double x, double shape) {
        // Approximation of the lower incomplete gamma function
        if (x <= 0) {
            return 0;
        }

        double sum = 1.0 / shape;
        double value = sum;
        for (int i = 1; i < 100; i++) { // Iterasi untuk presisi
            value *= x / (shape + i);
            sum += value;
        }
        return Math.exp(-x) * Math.pow(x, shape) * sum / gamma(shape);
    }

    public static double gamma(double x) {
        // Gamma function approximation (Lanczos approximation)
        double[] p = {
                0.99999999999980993, 676.5203681218851, -1259.1392167224028,
                771.32342877765313, -176.61502916214059, 12.507343278686905,
                -0.13857109526572012, 9.9843695780195716e-6,
                1.5056327351493116e-7
        };

        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gamma(1 - x));
        }

        x -= 1;
        double a = p[0];
        double t = x + 7.5;
        for (int i = 1; i < p.length; i++) {
            a += p[i] / (x + i);
        }
        return Math.sqrt(2 * Math.PI) * Math.pow(t, x + 0.5) * Math.exp(-t) * a;
    }
}
