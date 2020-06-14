package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class VasOffer {
    @NonNull
    String merchantId;
    @NonNull
    boolean aggregated;
}
