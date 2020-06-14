package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ServiceCapacityTrackerActivityOutput {
    @NonNull
    List<StoreCapacityDetailsBO> storeList;
}
