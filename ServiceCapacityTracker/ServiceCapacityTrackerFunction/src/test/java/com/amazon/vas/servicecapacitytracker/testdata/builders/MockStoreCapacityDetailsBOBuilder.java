package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.bo.StoreCapacityBO;
import com.amazon.vas.servicecapacitytracker.model.bo.StoreCapacityDetailsBO;
import com.google.common.collect.ImmutableMap;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MockStoreCapacityDetailsBOBuilder {
    private StoreCapacityDetailsBO storeCapacityDetailsBO;

    public StoreCapacityDetailsBO build() {
        return storeCapacityDetailsBO;
    }

    public MockStoreCapacityDetailsBOBuilder withAggregatedMerchants() {
        final Map<LocalDate, StoreCapacityBO> capacityMap = new HashMap<>();
        for (int idx = 0; idx < ConstantsClass.NUMBER_OF_DAYS; idx++)
            capacityMap.put(LocalDate.now().plusDays(idx), new MockStoreCapacityBOBuilder().build());
        storeCapacityDetailsBO = StoreCapacityDetailsBO.builder().storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).capacityMap(ImmutableMap.copyOf(capacityMap))
                .asin(ConstantsClass.ASIN).pinCode(ConstantsClass.PINCODE).build();
        return this;
    }

    public MockStoreCapacityDetailsBOBuilder withIndividualMerchants() {
        final Map<LocalDate, StoreCapacityBO> capacityMap = new HashMap<>();
        for (int idx = 0; idx < ConstantsClass.NUMBER_OF_DAYS; idx++)
            capacityMap.put(LocalDate.now().plusDays(idx), new MockStoreCapacityBOBuilder().build());
        storeCapacityDetailsBO = StoreCapacityDetailsBO.builder().storeName(ConstantsClass.INDIVIDUAL_MERCHANT_Name)
                .merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).capacityMap(ImmutableMap.copyOf(capacityMap))
                .asin(ConstantsClass.ASIN).pinCode(ConstantsClass.PINCODE).build();
        return this;
    }
}
