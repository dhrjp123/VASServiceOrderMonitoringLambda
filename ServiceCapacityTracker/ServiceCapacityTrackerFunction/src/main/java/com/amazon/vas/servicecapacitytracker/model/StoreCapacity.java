package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StoreCapacity {
    @NonNull
    private int totalCapacity;
    @NonNull
    private int availableCapacity;
}
