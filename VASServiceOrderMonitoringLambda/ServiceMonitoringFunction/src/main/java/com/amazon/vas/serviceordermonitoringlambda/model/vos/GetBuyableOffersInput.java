package com.amazon.vas.serviceordermonitoringlambda.model.vos;

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
