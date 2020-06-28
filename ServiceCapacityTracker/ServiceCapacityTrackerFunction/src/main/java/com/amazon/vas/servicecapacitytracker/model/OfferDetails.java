package com.amazon.vas.servicecapacitytracker.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class OfferDetails {
    @NonNull
    private String merchantId;
    @NonNull
    private boolean isAggregated;
}
