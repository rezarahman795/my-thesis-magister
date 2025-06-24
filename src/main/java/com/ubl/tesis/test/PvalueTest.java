package com.ubl.tesis.test;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 * @AUTHOR RR
 * @DATE 19/12/2024
 */
public class PvalueTest {
    public static void main(String[] args) {
            // Degrees of Freedom (df)
            int degreesOfFreedom = 255;

            // Signifikansi (alpha)
            double alpha = 0.05;

            // Membuat distribusi Chi-Square
            ChiSquaredDistribution chiDist = new ChiSquaredDistribution(degreesOfFreedom);

            // Menghitung nilai kritis (inverse cumulative probability)
            double criticalValue = chiDist.inverseCumulativeProbability(1 - alpha);

            // Menampilkan hasil
            System.out.printf("Nilai Kritis (Chi-Square Critical): %.5f%n", criticalValue);

    }
}
