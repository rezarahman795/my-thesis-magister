package com.ubl.tesis.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR RR
 * @DATE 20/12/2024
 */
public class EntrophyTests2 {
    // Method untuk menghitung entropi dari string Base64
    public static double calculateEntropy(String base64Ciphertext) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        int textLength = base64Ciphertext.length();

        // Hitung frekuensi setiap karakter dalam ciphertext
        for (char c : base64Ciphertext.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Hitung entropi berdasarkan frekuensi
        double entropy = 0.0;
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            double probability = (double) entry.getValue() / textLength;
            entropy -= probability * (Math.log(probability) / Math.log(2)); // Shannon entropy
        }

        return entropy;
    }

    public static void main(String[] args) {
        // Contoh ciphertext berbasis Base64
        String base64Ciphertext = "VRFZeHfT81en9ljVCk0vXFZcYjkTUE1E7Yk5/ltvj+fITvzb7GjSdFRs0H9W/GajYyTTOgR7QGEEU6FoChl50fOWod7K/p5Fya3MZJG9o/jwZa3qe5VrLzAWYKQQ5AKWNPTEPUfN+kaqNJEqBou/WYG7iIgHyMn6h66TzMXWDvFbGPEQg9JfJo+5jPDUBxPS61+Gi99qp8bcorML+qs9F6BT48tCHgTDtT9AAKPjtMzS0/hZlwQK3aK8D+fsbOFRoypb0m0QlqnLndAJSZrJ76t5OsAJf+HfzY/ITK+QY2FN6t6IbmRaH5LokvERXxOqEul5zL8GCUeZWD2OAWjimjqK3pWOpaCnUbDe2gPgRj1Sc8bW5hK0dziWoGrMUbIn9WvjSM7yRHexjcTa7zO0UAZAKzA1jQ1+Hhgr3Keg9qICeq5CdSsnd/eqtNE8uv+AXRL8jxjxNJFea/H3Y9oiipx/b5hRsO/da++WOdLCf/ISlrEGy5wfF1C33A1k2CoofVfbGyMloyzdOjaX54Tj9i/fobZCjmITf9XrAqc3RQS64CECInxtRKRrbkWaNRkRNfxY64Z55sghYjgpNv+Yx2F/nrzf6HEmeOHFxrkAZwqKzRKxepvyoYB8DKv8KtreLA046L6KeDrqsywUWj6L3TpbUQ8jYwvuOljqhcVU1DYypAeVAnt9szT3aCrpXUlLgjZ5EfpJzKnewPSYaXmawhu3QDRX/IGvPuck6eVb385iU38PLaX+SZ9KsM0kUa5hNoTWFGLIFEaQ0Qf6RA5kE90dbbm5bmNhDp0p/6/sOTp50KhSMuJAP3PMG42AeMVmRm/AN7m+wh62vVaq2C3QN11rggHNuluw6mjEUAFfJZlj3+66wKylyQcX992HT4oa6oRG3SYOL3SisAqT+WEqofw+TtuFcZpB5AXqZzPZpIF8Gu/Ns2lbi9ssTjqBOc+6EJRJtFicsgmYvBlAAWmBgNmCijM1uaofPsbvXCphO912tJWufjiAowO7hnEOVWCznWQzEvXa4vpk0YhJJ/I0L3DhegMU9NjeRsRSei8r+SF1pSKAaUN1IG8K6ZZUkb47b49KBr9vHAOGTKON0cMjrH8gXTVzWc7N9ju0LgA8DEriwjI4nOjseEJYQ9RdxfL/tBQhLdBY9vGWzf4O9mOpP6dzdjJzItZnB3tojebYwnoZ9o5WQgX7zT7wVY31MdyiqiYJ6qzFF/KkzaUv/ErDUXRCnAH0yuUyj/kXw3ndlbVxiQJ55mHq1GyezwBrXRuOLZ5vUBXPt6RY1pf+O+vU/AJ2J/hgOfblV2LQa0SP6VNQDs0WtJI5Z4y15qpUVHxx/xGrWLopykxNzS+Zp0kTLOaZQSBFy3yqAST2CTJrrqOCRaE1BBaCkXZIyRrm80pjkv8B3HwtckQ/U/2Iqd8MpFxrPDvISM9VeJD5EHFgAq0PGfsTepaTjUhqZ53pNhqeGnDzQJE2NYgLItL0i24bvdT4HLC4RpD6vZZNonLTlwQArY+1A34Y9k/Ouq8KQgz7eThJArtJvILTb/WIoDl6hXMnmY+Dj+7S5lyuNt+220eIPo89Zk++jJVn4bZuJ3ZJbnX8LpkfVCYGgYz5VhNnc+IrAdDOwPfea1dgZ+OvqXndc/HOYdEtCcT4BsZYfUMteHAu2Nh0IuTuZ9poo4sJIyVallDXcdm0VGm+6XX44ldiQtfYaUK/2jy1Ri7ppfF8";

        // Hitung entropi
        double entropy = calculateEntropy(base64Ciphertext);

        // Tampilkan hasil
        System.out.println("Base64 Ciphertext: " + base64Ciphertext);
        System.out.println("Entropy: " + entropy);
    }
}
