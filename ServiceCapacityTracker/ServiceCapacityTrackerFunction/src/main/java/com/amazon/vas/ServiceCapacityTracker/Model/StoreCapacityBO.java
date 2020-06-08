package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Builder
@Value
public class StoreCapacityBO
{
    int totalCapacity;
    int availableCapacity;
}
