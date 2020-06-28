package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class StoreCapacityBO {
    @NonNull
    private int totalCapacity;
    @NonNull
    private int availableCapacity;
}
