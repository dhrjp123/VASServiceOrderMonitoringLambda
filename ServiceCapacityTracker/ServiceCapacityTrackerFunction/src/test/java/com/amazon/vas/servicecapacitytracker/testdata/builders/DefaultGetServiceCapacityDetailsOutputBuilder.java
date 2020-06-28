package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacity;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetails;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class DefaultGetServiceCapacityDetailsOutputBuilder {
    private GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput;

    public DefaultGetServiceCapacityDetailsOutputBuilder() {
        withDefaultValues();
    }

    private void withDefaultValues() {
        final List<StoreCapacity> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityList.add(new DefaultStoreCapacityBuilder().build());
        final List<StoreCapacityDetails> storeList = ImmutableList.of(StoreCapacityDetails.builder()
                .storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME).merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID)
                .capacityList(ImmutableList.copyOf(capacityList)).asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE).build());
        getServiceCapacityDetailsOutput = GetServiceCapacityDetailsOutput.builder().storeList(storeList).build();
    }

    public GetServiceCapacityDetailsOutput build() {
        return getServiceCapacityDetailsOutput;
    }
}
