package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class ServiceCapacityDetailsBO {
    @NonNull
    List<StoreCapacityDetailsBO> storeList;
}
