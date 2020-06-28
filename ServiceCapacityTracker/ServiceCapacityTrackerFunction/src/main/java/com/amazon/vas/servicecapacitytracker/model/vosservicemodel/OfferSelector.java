package com.amazon.vas.servicecapacitytracker.model.vosservicemodel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class OfferSelector {
    @NonNull
    private AddressInput addressInput;
}
