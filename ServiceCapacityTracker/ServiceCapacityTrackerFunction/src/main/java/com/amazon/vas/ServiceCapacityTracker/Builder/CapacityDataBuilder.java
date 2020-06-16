package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItem;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantUniqueKeysBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class CapacityDataBuilder {
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    final private static int NUMBER_OF_COLUMNS = 7;
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;

    public Map<MerchantUniqueKeysBO, StoreCapacityBO> getCapacityMap(
            @NonNull final List<MerchantDetailsBO> merchantList) {
        final List<Object> itemsToGet = buildItemsToGet(merchantList);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        return getStoreCapacityMap(capacityItemMap.get(DYNAMODB_TABLE_NAME));
    }

    private Map<MerchantUniqueKeysBO, StoreCapacityBO> getStoreCapacityMap(final List<Object> capacityItemList) {
        Map<MerchantUniqueKeysBO, StoreCapacityBO> storeCapacityMap = new HashMap<>();
        for (Object capacityItemObject : capacityItemList) {
            CapacityDataItem capacityDataItem = (CapacityDataItem) capacityItemObject;
            storeCapacityMap.put(MerchantUniqueKeysBO.builder().Id(capacityDataItem.getId())
                    .date(capacityDataItem.getDate()).build(), StoreCapacityBO.builder()
                    .totalCapacity(capacityDataItem.getTotalCapacity())
                    .availableCapacity(capacityDataItem.getAvailableCapacity()).build());
        }
        return storeCapacityMap;
    }

    private List<Object> buildItemsToGet(final List<MerchantDetailsBO> merchantList) {
        List<Object> itemsToGet = new ArrayList<>();
        for (MerchantDetailsBO merchant : merchantList) {
            LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                CapacityDataItem capacityDataItem = new CapacityDataItem();
                capacityDataItem.setId(merchant.getAsin() + merchant.getMerchantId() + merchant.getPinCode());
                capacityDataItem.setDate(today.plusDays(date_idx).toString());
                itemsToGet.add(capacityDataItem);
            }
        }
        return itemsToGet;
    }
}
