package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class ServiceCapacityDetailsInputBO {
    @NonNull
    String skillType;
    @NonNull
    String storeName;
    @NonNull
    String marketplaceId;
    @NonNull
    int numberOfDays;
}
