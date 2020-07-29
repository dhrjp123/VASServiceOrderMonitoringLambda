package testdata.builders;

import constants.ConstantsClass;
import model.dynamodb.CapacityDataItem;

public class MockCapacityDataItemBuilder {
    private CapacityDataItem capacityDataItem;

    public MockCapacityDataItemBuilder(String date) {
        withDefaultValues(date);
    }

    private void withDefaultValues(String date) {
        capacityDataItem = new CapacityDataItem();
        capacityDataItem.setMarketplaceId(ConstantsClass.MARKETPLACE_ID);
        capacityDataItem.setAsin(ConstantsClass.ASIN);
        capacityDataItem.setMerchantId(ConstantsClass.AGGREGATED_MERCHANT_ID);
        capacityDataItem.setPinCode(ConstantsClass.PINCODE);
        capacityDataItem.setDate(date);
    }

    public CapacityDataItem build() {
        return capacityDataItem;
    }

    public MockCapacityDataItemBuilder withAllFields() {
        capacityDataItem.setStoreId(ConstantsClass.AGGREGATED_MERCHANT_STORE_ID);
        capacityDataItem.setTotalCapacity(ConstantsClass.TOTAL_CAPACITY);
        capacityDataItem.setAvailableCapacity(ConstantsClass.AVAILABLE_CAPACITY);
        return this;
    }
}
