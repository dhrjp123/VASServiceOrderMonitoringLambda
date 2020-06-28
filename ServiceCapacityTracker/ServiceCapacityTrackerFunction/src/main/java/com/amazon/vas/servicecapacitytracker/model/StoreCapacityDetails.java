package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

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
    private List<StoreCapacity> capacityList;
}
