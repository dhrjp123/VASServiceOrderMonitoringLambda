package com.amazon.vas.servicecapacitytracker.builder;

import com.amazon.vas.servicecapacitytracker.accessor.DynamoDbAccessor;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityBO;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBOBuilderInput;
import com.amazon.vas.servicecapacitytracker.model.dynamodbmodel.CapacityDataItem;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class StoreCapacityDetailsBOBuilder {
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;

    public List<StoreCapacityDetailsBO> getResponse(
            @NonNull final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList,
            @NonNull final int numberOfDays) {
        final List<CapacityDataItem> itemsToGet = buildItemsToGet(storeCapacityDetailsBOBuilderInputList, numberOfDays);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        return createResponse((List<CapacityDataItem>) (List) capacityItemMap.get(DYNAMODB_TABLE_NAME),
                storeCapacityDetailsBOBuilderInputList, numberOfDays);
    }

    private List<StoreCapacityDetailsBO> createResponse(final List<CapacityDataItem> capacityDataItemList,
                                                        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList,
                                                        final int numberOfDays) {
        Collections.sort(storeCapacityDetailsBOBuilderInputList);
        Collections.sort(capacityDataItemList);
        int currentIndexInCapacityDataItemList = 0;
        final List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput :
                storeCapacityDetailsBOBuilderInputList) {
            final List<CapacityDataItem> capacityDataItemSubListOfSingleMerchant = capacityDataItemList
                    .subList(currentIndexInCapacityDataItemList, currentIndexInCapacityDataItemList + numberOfDays);
            currentIndexInCapacityDataItemList += numberOfDays;
            final List<StoreCapacityBO> capacityList = capacityDataItemSubListOfSingleMerchant.stream()
                    .map(capacityDataItem -> StoreCapacityBO.builder()
                            .totalCapacity(capacityDataItem.getTotalCapacity())
                            .availableCapacity(capacityDataItem.getAvailableCapacity()).build())
                    .collect(Collectors.toList());
            storeList.add(StoreCapacityDetailsBO.builder().capacityList(capacityList)
                    .storeName(storeCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantName())
                    .merchantId(storeCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantId())
                    .asin(storeCapacityDetailsBOBuilderInput.getAsin())
                    .pinCode(storeCapacityDetailsBOBuilderInput.getPinCode()).build());
        }
        return storeList;
    }

    private List<CapacityDataItem> buildItemsToGet(
            final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList,
            final int numberOfDays) {
        final List<CapacityDataItem> itemsToGet = new ArrayList<>();
        for (StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput :
                storeCapacityDetailsBOBuilderInputList) {
            final LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                CapacityDataItem capacityDataItem = new CapacityDataItem();
                capacityDataItem.setAsin(storeCapacityDetailsBOBuilderInput.getAsin());
                capacityDataItem
                        .setMerchantId(storeCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantId());
                capacityDataItem.setPinCode(storeCapacityDetailsBOBuilderInput.getPinCode());
                capacityDataItem.setDate(today.plusDays(date_idx).toString());
                itemsToGet.add(capacityDataItem);
            }
        }
        return itemsToGet;
    }
}
