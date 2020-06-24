package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class ServiceCapacityDetailsBOBuilder {
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;

    public ServiceCapacityDetailsBO getResponse(
            @NonNull final List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList,
            int numberOfDays) {
        final List<Object> itemsToGet = buildItemsToGet(serviceCapacityDetailsBOBuilderInputList, numberOfDays);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        return createResponse((List<CapacityDataItem>) (List) capacityItemMap.get(DYNAMODB_TABLE_NAME),
                serviceCapacityDetailsBOBuilderInputList, numberOfDays);
    }

    private ServiceCapacityDetailsBO createResponse(final List<CapacityDataItem> capacityDataItemList,
                                                    final List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList,
                                                    final int numberOfDays) {
        Collections.sort(serviceCapacityDetailsBOBuilderInputList);
        Collections.sort(capacityDataItemList);
        int currentIndexInCapacityDataItemList = 0;
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (ServiceCapacityDetailsBOBuilderInput serviceCapacityDetailsBOBuilderInput :
                serviceCapacityDetailsBOBuilderInputList) {
            List<StoreCapacityBO> capacityList = new ArrayList<>();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                final CapacityDataItem capacityDataItem =
                        capacityDataItemList.get(currentIndexInCapacityDataItemList++);
                capacityList.add(StoreCapacityBO.builder().totalCapacity(capacityDataItem.getTotalCapacity())
                        .availableCapacity(capacityDataItem.getAvailableCapacity()).build());
            }
            storeList.add(StoreCapacityDetailsBO.builder().capacityList(capacityList)
                    .storeName(serviceCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantName())
                    .merchantId(serviceCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantId()).build());
        }
        Collections.sort(storeList, Comparator.comparing(StoreCapacityDetailsBO::getStoreName));
        return ServiceCapacityDetailsBO.builder().storeList(storeList).build();
    }

    private List<Object> buildItemsToGet(
            final List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList,
            int numberOfDays) {
        List<Object> itemsToGet = new ArrayList<>();
        for (ServiceCapacityDetailsBOBuilderInput serviceCapacityDetailsBOBuilderInput :
                serviceCapacityDetailsBOBuilderInputList) {
            final LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                CapacityDataItem capacityDataItem = new CapacityDataItem();
                capacityDataItem.setAsin(serviceCapacityDetailsBOBuilderInput.getAsin());
                capacityDataItem
                        .setMerchantId(serviceCapacityDetailsBOBuilderInput.getMerchantDetailsBO().getMerchantId());
                capacityDataItem.setPinCode(serviceCapacityDetailsBOBuilderInput.getPinCode());
                capacityDataItem.setDate(today.plusDays(date_idx).toString());
                itemsToGet.add(capacityDataItem);
            }
        }
        return itemsToGet;
    }
}
