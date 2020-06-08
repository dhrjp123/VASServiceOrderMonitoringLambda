package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Builder
@Value
public class ServiceCapacityTrackerRequestBO
{
    @NonNull
    String skillType;
    @NonNull
    String storeId;
    @NonNull
    String marketplaceId;
    @NonNull
    String pinCode;
}
