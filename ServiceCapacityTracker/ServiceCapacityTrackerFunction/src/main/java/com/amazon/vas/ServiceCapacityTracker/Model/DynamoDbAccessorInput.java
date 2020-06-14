package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class DynamoDbAccessorInput {
    @NonNull
    String asin;
    @NonNull
    String merchantId;
    @NonNull
    String pinCode;
}
