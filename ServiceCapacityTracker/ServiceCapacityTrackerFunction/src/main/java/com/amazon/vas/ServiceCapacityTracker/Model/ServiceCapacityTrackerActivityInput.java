package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ServiceCapacityTrackerActivityInput {
    @NonNull
    String skillType;
    @NonNull
    String storeName;
    @NonNull
    String marketplaceId;
}
