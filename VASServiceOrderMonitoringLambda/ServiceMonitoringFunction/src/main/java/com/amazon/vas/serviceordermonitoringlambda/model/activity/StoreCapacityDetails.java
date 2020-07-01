package com.amazon.vas.serviceordermonitoringlambda.model.activity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.Map;

@Value
@Builder
public class StoreCapacityDetails {
    @NonNull
    private String storeName;
    @NonNull
    private String merchantId;
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
    @NonNull
    private Map<LocalDate, StoreCapacity> capacityMap;
}
