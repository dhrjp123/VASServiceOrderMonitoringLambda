package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.dynamodbmodel.CapacityDataItem;

public class DefaultCapacityDataItemBuilder {
    private CapacityDataItem capacityDataItem;

    public DefaultCapacityDataItemBuilder(String date) {
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

    public DefaultCapacityDataItemBuilder withAllFields() {
        capacityDataItem.setStoreId(ConstantsClass.AGGREGATED_MERCHANT_STORE_ID);
        capacityDataItem.setTotalCapacity(ConstantsClass.TOTAL_CAPACITY);
        capacityDataItem.setAvailableCapacity(ConstantsClass.AVAILABLE_CAPACITY);
        return this;
    }
}
