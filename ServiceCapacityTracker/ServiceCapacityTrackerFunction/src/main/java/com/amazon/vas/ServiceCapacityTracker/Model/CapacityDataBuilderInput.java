package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CapacityDataBuilderInput {
    @NonNull
    private String merchantId;
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
}
