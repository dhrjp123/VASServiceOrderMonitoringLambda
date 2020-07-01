package com.amazon.vas.serviceordermonitoringlambda.testdata.builders;

import com.amazon.vas.serviceordermonitoringlambda.constants.ConstantsClass;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBOBuilderInput;

public class MockStoreCapacityDetailsBOBuilderInputBuilder {
    private StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput;

    public StoreCapacityDetailsBOBuilderInput build() {
        return storeCapacityDetailsBOBuilderInput;
    }

    public MockStoreCapacityDetailsBOBuilderInputBuilder withAggregatedMerchants() {
        storeCapacityDetailsBOBuilderInput = StoreCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new MockMerchantDetailsBOBuilder().withAggregatedMerchants().build()).build();
        return this;
    }

    public MockStoreCapacityDetailsBOBuilderInputBuilder withIndividualMerchants() {
        storeCapacityDetailsBOBuilderInput = StoreCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new MockMerchantDetailsBOBuilder().withIndividualMerchants().build()).build();
        return this;
    }
}
