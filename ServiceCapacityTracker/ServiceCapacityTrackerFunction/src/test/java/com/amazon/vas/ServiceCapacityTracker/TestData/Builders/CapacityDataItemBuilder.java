package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItem;

public class CapacityDataItemBuilder {
    private CapacityDataItem capacityDataItem;

    public CapacityDataItemBuilder(String date) {
        withDefaultValues(date);
    }

    private void withDefaultValues(String date) {
        capacityDataItem = new CapacityDataItem();
        capacityDataItem.setAsin(ConstantsClass.ASIN);
        capacityDataItem.setMerchantId(ConstantsClass.AGGREGATED_MERCHANT_ID);
        capacityDataItem.setPinCode(ConstantsClass.PINCODE);
        capacityDataItem.setDate(date);
    }

    public CapacityDataItem build() {
        return capacityDataItem;
    }

    public CapacityDataItemBuilder withAllFields() {
        capacityDataItem.setStoreId(ConstantsClass.AGGREGATED_MERCHANT_STORE_ID);
        capacityDataItem.setTotalCapacity(ConstantsClass.TOTAL_CAPACITY);
        capacityDataItem.setAvailableCapacity(ConstantsClass.AVAILABLE_CAPACITY);
        return this;
    }
}
