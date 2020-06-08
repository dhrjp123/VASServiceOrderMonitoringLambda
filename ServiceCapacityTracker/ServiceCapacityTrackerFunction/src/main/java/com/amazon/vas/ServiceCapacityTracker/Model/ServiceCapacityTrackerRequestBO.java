package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Builder
@Value
public class ServiceCapacityTrackerRequestBO
{
    String skillType;
    String storeId;
    String marketplaceId;
    String pinCode;
}
