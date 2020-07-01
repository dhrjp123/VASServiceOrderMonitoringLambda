package com.amazon.vas.serviceordermonitoringlambda.model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.util.Map;

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
    private Map<LocalDate, StoreCapacityBO> capacityMap;
}
