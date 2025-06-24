package com.ubl.tesis.controller;

import com.ubl.tesis.decrypt.AES_decrypt;
import com.ubl.tesis.request.RequestDataPelanggan;
import com.ubl.tesis.request.RequestDekrip;
import com.ubl.tesis.respone.APIResponse;
import com.ubl.tesis.service.PelangganService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @AUTHOR RR
 * @DATE 08/12/2024
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "informasi", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "AES API")
public class PelangganControllerAES {

    @Autowired
    private PelangganService pelangganService;

    @Autowired
    private AES_decrypt aesDecrypt;

    @PostMapping(value = "data-pelanggan-xyz-AES")
    public APIResponse<?> getDataPelangganAES(@RequestBody RequestDataPelanggan requestDataPelanggan) throws SQLException {
        try {

            return pelangganService.getDataPelanggan(requestDataPelanggan,true,false,false);
        } catch (Exception e) {
            return APIResponse.badRequest("Gagal");
        }
    }

    @PostMapping(value = "data-pelanggan-xyz-AES-dekrip")
    public String getDataPelangganAES_Dekrip(@RequestBody RequestDekrip req) throws SQLException {
        try {
            return AES_decrypt.decryptAES128bit(req);
        } catch (Exception e) {
            return "Data Tidak Ditemukan";
        }
    }
}