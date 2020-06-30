package com.amazon.vas.servicecapacitytracker.model.activity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetServiceCapacityDetailsInput {
    @NonNull
    private String skillType;
    @NonNull
    private String storeName;
    @NonNull
    private String marketplaceId;
    @NonNull
    private int numberOfDays;
}
