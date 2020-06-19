package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantDetailsBO;

public class MerchantDetailsBOBuilder {
    private MerchantDetailsBO merchantDetailsBO;

    public MerchantDetailsBO build() {
        return merchantDetailsBO;
    }

    public MerchantDetailsBOBuilder forAggregatedMerchants() {
        merchantDetailsBO = MerchantDetailsBO.builder().merchantName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).build();
        return this;
    }

    public MerchantDetailsBOBuilder forIndividualMerchants() {
        merchantDetailsBO =
                MerchantDetailsBO.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .merchantName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).build();
        return this;
    }
}
