package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataBuilderInput;

public class CapacityDataBuilderInputBuilder {
    private CapacityDataBuilderInput capacityDataBuilderInput;

    public CapacityDataBuilderInput build() {
        return capacityDataBuilderInput;
    }

    public CapacityDataBuilderInputBuilder forAggregatedMerchants() {
        capacityDataBuilderInput = CapacityDataBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE).merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).build();
        return this;
    }

    public CapacityDataBuilderInputBuilder forIndividualMerchants() {
        capacityDataBuilderInput = CapacityDataBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE).merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).build();
        return this;
    }
}
