package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class MerchantDetailsBO {
    @NonNull
    String merchantName;
    @NonNull
    String merchantId;
    @NonNull
    String pinCode;
    @NonNull
    String asin;
}
