package com.amazon.vas.serviceordermonitoringlambda.model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class ServiceCapacityDetailsInputBO {
    @NonNull
    private String skillType;
    @NonNull
    private String storeName;
    @NonNull
    private String marketplaceId;
    @NonNull
    private int numberOfDays;
}
