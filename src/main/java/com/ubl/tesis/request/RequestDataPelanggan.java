/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubl.tesis.request;

import lombok.*;

import java.io.Serializable;

/**
 *
 * @author USER
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestDataPelanggan implements Serializable{
    private String idpel;
    private String jenis_transaksi;
}
