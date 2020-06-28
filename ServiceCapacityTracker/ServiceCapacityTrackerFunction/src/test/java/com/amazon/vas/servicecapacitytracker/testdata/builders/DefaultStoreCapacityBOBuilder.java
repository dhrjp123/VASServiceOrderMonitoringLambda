package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityBO;

public class DefaultStoreCapacityBOBuilder {
    private StoreCapacityBO storeCapacityBO;

    public DefaultStoreCapacityBOBuilder() {
        withDefaultValues();
    }

    private void withDefaultValues() {
        storeCapacityBO = StoreCapacityBO.builder().availableCapacity(ConstantsClass.AVAILABLE_CAPACITY)
                .totalCapacity(ConstantsClass.TOTAL_CAPACITY).build();
    }

    public StoreCapacityBO build() {
        return storeCapacityBO;
    }
}
