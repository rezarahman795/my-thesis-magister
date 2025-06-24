package com.ubl.tesis.controller;

import com.ubl.tesis.request.RequestDataPelanggan;
import com.ubl.tesis.respone.APIResponse;
import com.ubl.tesis.service.PelangganService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @AUTHOR RR
 * @DATE 01/12/2024
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "informasi", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "ORIGINAL API")
public class PelangganController {

    @Autowired
    private PelangganService pelangganService;

    @PostMapping(value = "data-pelanggan-xyz")
    public APIResponse<?> getDataPelanggan(@RequestBody RequestDataPelanggan requestDataPelanggan) throws SQLException {
        try {
            return pelangganService.getDataPelanggan(requestDataPelanggan,false,false,false);
        } catch (Exception e) {
            return APIResponse.badRequest("Gagal");
        }
    }
}
