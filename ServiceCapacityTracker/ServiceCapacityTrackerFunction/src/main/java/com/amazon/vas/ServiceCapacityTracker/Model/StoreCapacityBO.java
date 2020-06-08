package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

@Data
@Builder
@Value
public class StoreCapacityBO
{
    private int totalCapacity;
    private int availableCapacity;
}
