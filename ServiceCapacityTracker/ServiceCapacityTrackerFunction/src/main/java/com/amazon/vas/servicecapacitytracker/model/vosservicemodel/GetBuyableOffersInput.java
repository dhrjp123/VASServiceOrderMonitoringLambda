package com.amazon.vas.servicecapacitytracker.model.vosservicemodel;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetBuyableOffersInput {
    @NonNull
    private String asin;
    @NonNull
    private String marketplaceId;
    @NonNull
    private OfferSelector offerSelector;
}
