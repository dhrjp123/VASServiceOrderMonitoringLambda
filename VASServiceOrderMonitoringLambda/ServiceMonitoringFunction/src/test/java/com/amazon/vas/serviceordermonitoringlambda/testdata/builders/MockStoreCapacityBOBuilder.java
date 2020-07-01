package com.amazon.vas.serviceordermonitoringlambda.testdata.builders;

import com.amazon.vas.serviceordermonitoringlambda.constants.ConstantsClass;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityBO;

public class MockStoreCapacityBOBuilder {
    private StoreCapacityBO storeCapacityBO;

    public MockStoreCapacityBOBuilder() {
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
