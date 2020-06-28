package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StoreCapacityDetailsBOBuilderInput implements Comparable {
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
    @NonNull
    private MerchantDetailsBO merchantDetailsBO;

    public int compareTo(Object object) {
        StoreCapacityDetailsBOBuilderInput storeCapacityDetailsBOBuilderInput =
                (StoreCapacityDetailsBOBuilderInput) object;
        return merchantDetailsBO.getMerchantId()
                .compareTo(storeCapacityDetailsBOBuilderInput.merchantDetailsBO.getMerchantId());
    }
}
