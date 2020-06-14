package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetMerchantAggregatedDetailsInput {
    @NonNull
    String encryptedMarketplaceId;
    @NonNull
    String encryptedMerchantId;
}
