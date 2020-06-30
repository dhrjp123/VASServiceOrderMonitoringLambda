package com.amazon.vas.servicecapacitytracker.model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class CityDetailsBO {
    @NonNull
    private String pinCode;
    @NonNull
    private String merchantId;
}
