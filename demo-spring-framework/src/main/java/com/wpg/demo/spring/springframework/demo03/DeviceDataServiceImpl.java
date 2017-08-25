package com.wpg.demo.spring.springframework.demo03;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ChangWei Li
 * @version 2017-08-25 17:07
 */
@Service
@Transactional(readOnly = true)
public class DeviceDataServiceImpl implements DeviceDataService {

    private final DeviceDataRepository deviceDataRepository;

    public DeviceDataServiceImpl(DeviceDataRepository deviceDataRepository) {
        this.deviceDataRepository = deviceDataRepository;
    }

    @Override
    public List<Map<String, Object>> queryLatestDeviceData(String deviceId) {
        return deviceDataRepository.queryLatestDeviceData(deviceId);
    }
}
