package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacity;

public class DefaultStoreCapacityBuilder {
    private StoreCapacity storeCapacity;

    public DefaultStoreCapacityBuilder() {
        withDefaultValues();
    }

    private void withDefaultValues() {
        storeCapacity = StoreCapacity.builder().totalCapacity(ConstantsClass.TOTAL_CAPACITY)
                .availableCapacity(ConstantsClass.AVAILABLE_CAPACITY).build();
    }

    public StoreCapacity build() {
        return storeCapacity;
    }
}
