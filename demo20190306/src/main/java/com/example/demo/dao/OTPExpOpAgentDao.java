package com.example.demo.dao;

import com.example.demo.entity.OtpExpOpAgent;
import com.example.demo.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OTPExpOpAgentDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<OtpExpOpAgent> getAgentlist() {
        String sql = "select waybill_no,op_code,org_code,create_time,aux_op_code from otp_exp_op_agent  " +
                "where increment_id < 2 ";

//        System.out.println(sql);


        List<OtpExpOpAgent> agentList = jdbcTemplate.query(sql, new RowMapper<OtpExpOpAgent>() {

            @Override
            public OtpExpOpAgent mapRow(ResultSet resultSet, int i) throws SQLException {
                OtpExpOpAgent anent = new OtpExpOpAgent();
                anent.setWaybill_no(resultSet.getString("waybill_no"));
                anent.setOp_code(resultSet.getString("op_code"));
                anent.setOrg_code(resultSet.getString("org_code"));
                anent.setCreate_time(resultSet.getTimestamp("create_time"));
                anent.setAux_op_code(resultSet.getString("aux_op_code"));
//                System.out.println(anent.getWaybill_no());
                return anent;

            }
        });
        return agentList;
    }

}
