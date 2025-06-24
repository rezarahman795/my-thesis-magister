package com.ubl.tesis.request;

import lombok.*;

/**
 * @AUTHOR RR
 * @DATE 11/12/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestDekrip {
    private String key;
    private String cipherText;

}
