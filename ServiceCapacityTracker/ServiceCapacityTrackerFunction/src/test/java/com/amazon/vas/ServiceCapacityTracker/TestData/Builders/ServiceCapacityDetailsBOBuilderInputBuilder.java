package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBOBuilderInput;

public class ServiceCapacityDetailsBOBuilderInputBuilder {
    private ServiceCapacityDetailsBOBuilderInput serviceCapacityDetailsBOBuilderInput;

    public ServiceCapacityDetailsBOBuilderInput build() {
        return serviceCapacityDetailsBOBuilderInput;
    }

    public ServiceCapacityDetailsBOBuilderInputBuilder forAggregatedMerchants() {
        serviceCapacityDetailsBOBuilderInput = ServiceCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new MerchantDetailsBOBuilder().forAggregatedMerchants().build()).build();
        return this;
    }

    public ServiceCapacityDetailsBOBuilderInputBuilder forIndividualMerchants() {
        serviceCapacityDetailsBOBuilderInput = ServiceCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new MerchantDetailsBOBuilder().forIndividualMerchants().build()).build();
        return this;
    }
}
