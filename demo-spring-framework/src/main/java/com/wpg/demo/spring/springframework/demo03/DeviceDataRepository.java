package com.wpg.demo.spring.springframework.demo03;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ChangWei Li
 * @version 2017-08-25 17:06
 */
@Repository
public class DeviceDataRepository {

    private static final String SQL_LATEST_DATA = "SELECT sv.* FROM(SELECT ID_DEVICE, ID_SERVICE, max(DATE_TIME) AS DATE_TIME FROM service_values_20170823 WHERE ID_DEVICE = '15040038' GROUP BY ID_DEVICE, ID_SERVICE) svt LEFT JOIN service_values_20170823 sv ON sv.ID_DEVICE = svt.ID_DEVICE AND sv.ID_SERVICE = svt.ID_SERVICE AND sv.DATE_TIME = svt.DATE_TIME";

    private final JdbcTemplate jdbcTemplate;

    public DeviceDataRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> queryLatestDeviceData(String deviceId) {
        return jdbcTemplate.queryForList(SQL_LATEST_DATA);
    }

}
