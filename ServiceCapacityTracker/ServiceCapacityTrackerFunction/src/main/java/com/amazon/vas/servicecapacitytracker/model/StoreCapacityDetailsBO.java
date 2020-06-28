package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class StoreCapacityDetailsBO {
    @NonNull
    private String storeName;
    @NonNull
    private String merchantId;
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
    @NonNull
    private List<StoreCapacityBO> capacityList;
}
