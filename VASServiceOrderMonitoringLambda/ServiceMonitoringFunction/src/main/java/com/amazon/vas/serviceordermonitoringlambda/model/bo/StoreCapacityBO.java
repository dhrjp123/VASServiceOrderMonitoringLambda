package com.amazon.vas.serviceordermonitoringlambda.model.bo;

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
