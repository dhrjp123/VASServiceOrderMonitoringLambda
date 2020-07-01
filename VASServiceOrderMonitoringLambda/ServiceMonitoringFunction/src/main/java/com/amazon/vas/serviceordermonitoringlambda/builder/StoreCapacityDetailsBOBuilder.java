package com.amazon.vas.serviceordermonitoringlambda.builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.DynamoDbAccessor;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBOBuilderInput;
import com.amazon.vas.serviceordermonitoringlambda.model.dynamodb.CapacityDataItem;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class StoreCapacityDetailsBOBuilder {
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;

    public List<StoreCapacityDetailsBO> getResponse(@NonNull final String marketplaceId,
                                                    @NonNull final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList,
                                                    @NonNull final int numberOfDays) {
        final List<CapacityDataItem> itemsToGet = buildItemsToGet(marketplaceId,
                storeCapacityDetailsBOBuilderInputList, numberOfDays);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet.stream().map(
                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList()));
        final Map<String, Map<LocalDate, StoreCapacityBO>> merchantCapacityMap =
                getMerchantCapacityMap(
                        (List<CapacityDataItem>) (List) capacityItemMap.get(CapacityDataItem.TABLE_NAME));
        return createResponse(merchantCapacityMap, storeCapacityDetailsBOBuilderInputList);
    }

    private List<StoreCapacityDetailsBO> createResponse(
            final Map<String, Map<LocalDate, StoreCapacityBO>> merchantCapacityMap,
            final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList) {
        return storeCapacityDetailsBOBuilderInputList.stream()
                .map(input -> StoreCapacityDetailsBO.builder().asin(input.getAsin()).pinCode(input.getPinCode())
                        .storeName(input.getMerchantDetailsBO().getMerchantName())
                        .merchantId(input.getMerchantDetailsBO().getMerchantId())
                        .capacityMap(merchantCapacityMap.get(input.getMerchantDetailsBO().getMerchantId())).build())
                .collect(Collectors.toList());
    }

    private Map<String, Map<LocalDate, StoreCapacityBO>> getMerchantCapacityMap(
            final List<CapacityDataItem> capacityDataItemList) {
        final Map<String, Map<LocalDate, StoreCapacityBO>> merchantCapacityMap = new HashMap<>();
        for (CapacityDataItem capacityDataItem : capacityDataItemList) {
            final String merchantId = capacityDataItem.getMerchantId();
            Map<LocalDate, StoreCapacityBO> storeCapacityMap;
            if (merchantCapacityMap.containsKey(merchantId)) {
                storeCapacityMap = merchantCapacityMap.get(merchantId);
            } else {
                storeCapacityMap = new HashMap<>();
            }
            storeCapacityMap.put(LocalDate.parse(capacityDataItem.getDate()), StoreCapacityBO.builder()
                    .totalCapacity(capacityDataItem.getTotalCapacity())
                    .availableCapacity(capacityDataItem.getAvailableCapacity()).build());
            merchantCapacityMap.put(merchantId, storeCapacityMap);
        }
        return merchantCapacityMap;
    }

    private List<CapacityDataItem> buildItemsToGet(final String marketplaceId,
                                                   final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList,
                                                   final int numberOfDays) {
        final List<CapacityDataItem> itemsToGet = new ArrayList<>();
        for (StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput
                : storeCapacityDetailsBOBuilderInputList) {
            final LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                CapacityDataItem capacityDataItem = new CapacityDataItem();
                capacityDataItem.setMarketplaceId(marketplaceId);
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
