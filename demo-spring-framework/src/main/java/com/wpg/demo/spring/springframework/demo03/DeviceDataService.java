package com.wpg.demo.spring.springframework.demo03;

import java.util.List;
import java.util.Map;

/**
 * @author ChangWei Li
 * @version 2017-08-25 17:07
 */
public interface DeviceDataService {

    List<Map<String, Object>> queryLatestDeviceData(String deviceId);

}
