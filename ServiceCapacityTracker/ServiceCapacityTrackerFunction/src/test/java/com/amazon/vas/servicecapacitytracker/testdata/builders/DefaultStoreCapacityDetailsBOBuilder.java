package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityBO;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBO;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class DefaultStoreCapacityDetailsBOBuilder {
    StoreCapacityDetailsBO storeCapacityDetailsBO;

    public StoreCapacityDetailsBO build() {
        return storeCapacityDetailsBO;
    }

    public DefaultStoreCapacityDetailsBOBuilder forAggregatedMerchants() {
        final List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int idx = 0; idx < ConstantsClass.NUMBER_OF_COLUMNS; idx++)
            capacityList.add(new DefaultStoreCapacityBOBuilder().build());
        storeCapacityDetailsBO = StoreCapacityDetailsBO.builder().storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).capacityList(ImmutableList.copyOf(capacityList))
                .asin(ConstantsClass.ASIN).pinCode(ConstantsClass.PINCODE).build();
        return this;
    }

    public DefaultStoreCapacityDetailsBOBuilder forIndividualMerchants() {
        final List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int idx = 0; idx < ConstantsClass.NUMBER_OF_COLUMNS; idx++)
            capacityList.add(new DefaultStoreCapacityBOBuilder().build());
        storeCapacityDetailsBO = StoreCapacityDetailsBO.builder().storeName(ConstantsClass.INDIVIDUAL_MERCHANT_Name)
                .merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).capacityList(ImmutableList.copyOf(capacityList))
                .asin(ConstantsClass.ASIN).pinCode(ConstantsClass.PINCODE).build();
        return this;
    }
}
