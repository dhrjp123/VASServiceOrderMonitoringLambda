package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.bo.MerchantDetailsBO;

public class MockMerchantDetailsBOBuilder {
    private MerchantDetailsBO merchantDetailsBO;

    public MerchantDetailsBO build() {
        return merchantDetailsBO;
    }

    public MockMerchantDetailsBOBuilder withAggregatedMerchants() {
        merchantDetailsBO = MerchantDetailsBO.builder().merchantName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).build();
        return this;
    }

    public MockMerchantDetailsBOBuilder withIndividualMerchants() {
        merchantDetailsBO =
                MerchantDetailsBO.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .merchantName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).build();
        return this;
    }
}
