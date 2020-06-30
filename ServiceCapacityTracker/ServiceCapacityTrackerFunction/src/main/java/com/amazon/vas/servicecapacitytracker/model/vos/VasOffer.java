package com.amazon.vas.servicecapacitytracker.model.vos;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class VasOffer {
    @NonNull
    private String merchantId;
    @NonNull
    private boolean aggregated;
}
