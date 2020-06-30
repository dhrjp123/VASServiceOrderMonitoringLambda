package com.amazon.vas.servicecapacitytracker.model.vos;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class AddressInput {
    @NonNull
    private String postalCode;
    private String countryCode;
}
