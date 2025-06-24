package com.ubl.tesis.service;

import com.ubl.tesis.encrypt.AES;
import com.ubl.tesis.encrypt.Blowfish;
import com.ubl.tesis.encrypt.Hybrid;
import com.ubl.tesis.request.RequestDataPelanggan;
import com.ubl.tesis.respone.APIResponse;
import com.ubl.tesis.util.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @AUTHOR RR
 * @DATE 01/12/2024
 */
@Service
@Slf4j
@Repository
public class PelangganService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public APIResponse<?>getDataPelanggan(RequestDataPelanggan getRequest, final boolean aes, final boolean blowfish, final boolean hybrid) throws SQLException {
        APIResponse res = new APIResponse();
        List<Map<String, Object>> lmap = new ArrayList<>();
        Connection con = null;
        CallableStatement call = null;
        String out_message = "";
        Integer out_return = null;
        try {
            con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            String sql = "{? = call isidatabaseanda(?,?,?,?)}";
            call = con.prepareCall(sql);
            call.registerOutParameter(1, Types.INTEGER);
            call.setString(2, getRequest.getIdpel());
            call.setString(3, getRequest.getJenis_transaksi());
            call.registerOutParameter(4, Types.VARCHAR);
            call.registerOutParameter(5, Types.REF_CURSOR);

            call.execute();
            out_return = call.getInt(1);
            out_message = call.getString(4);
            ResultSet rs = null;
            if (out_return == 1) {
                rs = (ResultSet) call.getObject(5);
                lmap = AppUtil.convertResultsetToListStrV2(rs);
                rs.close();
            }

            // populate response
            res.setSuccess(!lmap.isEmpty());
            res.setMessage(out_message);
            res.setStatus(out_return != 1 ? 400 : 200);

            if (aes){
                res.setData(lmap.isEmpty() ? null : AES.encryptAES128(lmap.getFirst()));
            }else if (blowfish){
                res.setData(lmap.isEmpty() ? null : Blowfish.BlowfishCryptography(lmap.getFirst()));
            }else if (hybrid){
                res.setData(lmap.isEmpty() ? null : Hybrid.encryptWrap(String.valueOf(lmap.getFirst())));
            }else{
                res.setData(lmap.isEmpty() ? null : lmap);
            }


            // close CallableStatement dan Connection
            call.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error Get Pelanggan By Parameter, {}", ex.getLocalizedMessage());
            res.setSuccess(false);
            res.setMessage("Error Pelanggan By Parameter, " + ex.getLocalizedMessage());
            res.setData(null);
        } finally {
            assert call != null;
            call.close();
            con.close();
        }
        return res;
    }
}
