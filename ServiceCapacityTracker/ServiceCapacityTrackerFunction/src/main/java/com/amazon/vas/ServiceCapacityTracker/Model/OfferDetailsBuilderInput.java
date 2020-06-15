package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class OfferDetailsBuilderInput
{
    @NonNull
    String asin;
    @NonNull
    String pinCode;
    @NonNull
    String marketplaceId;
}
