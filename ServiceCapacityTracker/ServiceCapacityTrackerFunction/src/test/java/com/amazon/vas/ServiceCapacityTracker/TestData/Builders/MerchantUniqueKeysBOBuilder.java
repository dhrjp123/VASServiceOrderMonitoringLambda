package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItemUniqueKeys;

public class MerchantUniqueKeysBOBuilder {
    private CapacityDataItemUniqueKeys capacityDataItemUniqueKeys;

    public CapacityDataItemUniqueKeys build() {
        return capacityDataItemUniqueKeys;
    }

    public MerchantUniqueKeysBOBuilder forAggregatedMerchants(String date) {
        capacityDataItemUniqueKeys =
                CapacityDataItemUniqueKeys.builder().Id(ConstantsClass.AGGREGATED_MERCHANT_UNIQUE_Id)
                        .date(date).build();
        return this;
    }

    public MerchantUniqueKeysBOBuilder forIndividualMerchants(String date) {
        capacityDataItemUniqueKeys =
                CapacityDataItemUniqueKeys.builder().Id(ConstantsClass.INDIVIDUAL_MERCHANT_UNIQUE_ID)
                        .date(date).build();
        return this;
    }
}
