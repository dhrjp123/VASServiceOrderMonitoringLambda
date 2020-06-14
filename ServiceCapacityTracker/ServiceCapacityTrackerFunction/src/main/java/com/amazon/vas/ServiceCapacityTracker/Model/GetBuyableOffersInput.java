package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetBuyableOffersInput {
    @NonNull
    String asin;
    @NonNull
    String marketplaceId;
    @NonNull
    OfferSelector offerSelector;
}
