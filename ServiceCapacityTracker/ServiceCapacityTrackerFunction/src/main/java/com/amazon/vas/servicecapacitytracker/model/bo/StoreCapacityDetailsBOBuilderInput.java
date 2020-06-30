package com.amazon.vas.servicecapacitytracker.model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StoreCapacityDetailsBOBuilderInput {
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
    @NonNull
    private MerchantDetailsBO merchantDetailsBO;
}
