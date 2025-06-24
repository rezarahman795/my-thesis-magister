package com.ubl.tesis.controller;

import com.ubl.tesis.decrypt.AES_decrypt;
import com.ubl.tesis.decrypt.Blowfish_decrypt;
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
@Tag(name = "BLOWFISH API")
public class PelangganControllerBlowfish {

    @Autowired
    private PelangganService pelangganService;

    @PostMapping(value = "data-pelanggan-xyz-blowfish")
    public APIResponse<?> getDataPelangganBlowfish(@RequestBody RequestDataPelanggan requestDataPelanggan) throws SQLException {
        try {
            return pelangganService.getDataPelanggan(requestDataPelanggan,false,true,false);
        } catch (Exception e) {
            return APIResponse.badRequest("Data Tidak Ditemukan");
        }
    }

    @PostMapping(value = "data-pelanggan-xyz-blowfish-dekrip")
    public String getDataPelangganBlowfish_Dekrip(@RequestBody RequestDekrip dekrip) throws SQLException {
        try {
            return Blowfish_decrypt.BlowfishDecryption(dekrip);
        } catch (Exception e) {
            return "Data Tidak Ditemukan";
        }
    }
}