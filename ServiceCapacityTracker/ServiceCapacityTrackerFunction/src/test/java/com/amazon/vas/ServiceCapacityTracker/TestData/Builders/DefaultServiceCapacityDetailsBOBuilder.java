package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityDetailsBO;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class DefaultServiceCapacityDetailsBOBuilder {
    private ServiceCapacityDetailsBO serviceCapacityDetailsBO;

    public ServiceCapacityDetailsBO build() {
        return serviceCapacityDetailsBO;
    }

    public DefaultServiceCapacityDetailsBOBuilder forAggregatedMerchants() {
        List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityList.add(new StoreCapacityBOBuilder().build());
        ImmutableList<StoreCapacityDetailsBO> storeList = ImmutableList.of(StoreCapacityDetailsBO.builder()
                .storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME).merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID)
                .capacityList(ImmutableList.copyOf(capacityList)).build());
        serviceCapacityDetailsBO = ServiceCapacityDetailsBO.builder().storeList(storeList).build();
        return this;
    }

    public DefaultServiceCapacityDetailsBOBuilder forIndividualMerchants() {
        List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityList.add(new StoreCapacityBOBuilder().build());
        ImmutableList<StoreCapacityDetailsBO> storeList = ImmutableList.of(StoreCapacityDetailsBO.builder()
                .storeName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                .capacityList(ImmutableList.copyOf(capacityList)).build());
        serviceCapacityDetailsBO = ServiceCapacityDetailsBO.builder().storeList(storeList).build();
        return this;
    }
}
