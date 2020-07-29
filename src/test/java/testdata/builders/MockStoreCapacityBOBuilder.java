package testdata.builders;

import constants.ConstantsClass;
import model.bo.StoreCapacityBO;

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
