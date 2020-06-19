package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;

public class StoreCapacityBOBuilder {
    private StoreCapacityBO storeCapacityBO;

    public StoreCapacityBOBuilder() {
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
