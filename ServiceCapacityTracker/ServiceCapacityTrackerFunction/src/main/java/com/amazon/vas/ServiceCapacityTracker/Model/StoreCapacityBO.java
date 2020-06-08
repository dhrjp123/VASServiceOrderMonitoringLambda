package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Builder
@Value
public class StoreCapacityBO
{
    @NonNull
    int totalCapacity;
    @NonNull
    int availableCapacity;
}
