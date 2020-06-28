package com.amazon.vas.servicecapacitytracker.config;

import com.amazon.vas.servicecapacitytracker.model.CityDetailsBO;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    public Map<String, CityDetailsBO> getCityMapper() {
        Map<String, CityDetailsBO> cityMap = new HashMap<>();
        cityMap.put("Delhi", CityDetailsBO.builder().merchantId("CMID1").pinCode("473001").build());
        cityMap.put("Jaipur", CityDetailsBO.builder().merchantId("CMID2").pinCode("462003").build());
        return cityMap;
    }

    public Map<String, String> getAsinMapper() {
        Map<String, String> asinMap = new HashMap<>();
        asinMap.put("AC Installation", "AID1");
        asinMap.put("TV Installation", "AID2");
        asinMap.put("Fan Installation", "AID3");
        return asinMap;
    }
}
