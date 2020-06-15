package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetServiceCapacityDetailsInput {
    @NonNull
    String skillType;
    @NonNull
    String storeName;
    @NonNull
    String marketplaceId;
}
