package com.amazon.vas.servicecapacitytracker.model.spinservicemodel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetMerchantAggregatedDetailsOutput {
    @NonNull
    private MerchantAggregatedDetails merchantAggregatedDetails;
}
