package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBOBuilderInput;

public class DefaultStoreCapacityDetailsBOBuilderInputBuilder {
    private StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput;

    public StoreCapacityDetailsBOBuilderInput build() {
        return storeCapacityDetailsBOBuilderInput;
    }

    public DefaultStoreCapacityDetailsBOBuilderInputBuilder forAggregatedMerchants() {
        storeCapacityDetailsBOBuilderInput = StoreCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new DefaultMerchantDetailsBOBuilder().forAggregatedMerchants().build()).build();
        return this;
    }

    public DefaultStoreCapacityDetailsBOBuilderInputBuilder forIndividualMerchants() {
        storeCapacityDetailsBOBuilderInput = StoreCapacityDetailsBOBuilderInput.builder().asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE)
                .merchantDetailsBO(new DefaultMerchantDetailsBOBuilder().forIndividualMerchants().build()).build();
        return this;
    }
}
