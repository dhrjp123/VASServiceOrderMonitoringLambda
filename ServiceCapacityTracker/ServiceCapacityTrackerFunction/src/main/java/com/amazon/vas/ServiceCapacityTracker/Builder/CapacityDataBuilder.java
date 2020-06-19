package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataBuilderInput;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItem;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItemUniqueKeys;
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
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;

    public Map<CapacityDataItemUniqueKeys, StoreCapacityBO> getCapacityMap(
            @NonNull final List<CapacityDataBuilderInput> capacityDataBuilderInputList, int numberOfDays) {
        final List<Object> itemsToGet = buildItemsToGet(capacityDataBuilderInputList, numberOfDays);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        return getStoreCapacityMap(capacityItemMap.get(DYNAMODB_TABLE_NAME));
    }

    private Map<CapacityDataItemUniqueKeys, StoreCapacityBO> getStoreCapacityMap(final List<Object> capacityItemList) {
        Map<CapacityDataItemUniqueKeys, StoreCapacityBO> storeCapacityMap = new HashMap<>();
        for (Object capacityItemObject : capacityItemList) {
            CapacityDataItem capacityDataItem = (CapacityDataItem) capacityItemObject;
            storeCapacityMap.put(CapacityDataItemUniqueKeys.builder().Id(capacityDataItem.getId())
                    .date(capacityDataItem.getDate()).build(), StoreCapacityBO.builder()
                    .totalCapacity(capacityDataItem.getTotalCapacity())
                    .availableCapacity(capacityDataItem.getAvailableCapacity()).build());
        }
        return storeCapacityMap;
    }

    private List<Object> buildItemsToGet(final List<CapacityDataBuilderInput> capacityDataBuilderInputList,
                                         int numberOfDays) {
        List<Object> itemsToGet = new ArrayList<>();
        for (CapacityDataBuilderInput capacityDataBuilderInput : capacityDataBuilderInputList) {
            LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                CapacityDataItem capacityDataItem = new CapacityDataItem();
                capacityDataItem.setAsin(capacityDataBuilderInput.getAsin());
                capacityDataItem.setMerchantId(capacityDataBuilderInput.getMerchantId());
                capacityDataItem.setPinCode(capacityDataBuilderInput.getPinCode());
                capacityDataItem.setDate(today.plusDays(date_idx).toString());
                itemsToGet.add(capacityDataItem);
            }
        }
        return itemsToGet;
    }
}
