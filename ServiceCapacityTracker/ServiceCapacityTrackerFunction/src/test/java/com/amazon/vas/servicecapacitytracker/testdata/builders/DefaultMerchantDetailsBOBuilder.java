package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.MerchantDetailsBO;

public class DefaultMerchantDetailsBOBuilder {
    private MerchantDetailsBO merchantDetailsBO;

    public MerchantDetailsBO build() {
        return merchantDetailsBO;
    }

    public DefaultMerchantDetailsBOBuilder forAggregatedMerchants() {
        merchantDetailsBO = MerchantDetailsBO.builder().merchantName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).build();
        return this;
    }

    public DefaultMerchantDetailsBOBuilder forIndividualMerchants() {
        merchantDetailsBO =
                MerchantDetailsBO.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .merchantName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).build();
        return this;
    }
}
