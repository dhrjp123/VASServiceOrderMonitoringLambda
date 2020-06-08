package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Data
@Builder
@Value
public class ServiceCapacityTrackerRequestBO
{
    private String skillType;
    private String storeId;
    private String marketplaceId;
    private String pinCode;
}
