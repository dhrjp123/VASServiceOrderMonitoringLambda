package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class OfferDetailsBuilder
{
    @NonNull
    private final VOSServiceAccessor vosServiceAccessor;
    public List<VasOffer> getVasOfferList(@NonNull final OfferDetailsBuilderInput offerDetailsBuilderInput) {
        final AddressInput addressInput = AddressInput.builder().postalCode(offerDetailsBuilderInput.getPinCode()).build();
        final OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        final GetBuyableOffersInput getBuyableOffersInput = GetBuyableOffersInput.builder()
                .asin(offerDetailsBuilderInput.getAsin()).marketplaceId(offerDetailsBuilderInput.getMarketplaceId())
                .offerSelector(offerSelector).build();
        final List<VasOffer> VasOfferList = vosServiceAccessor.getUnderlyingMerchantsId(getBuyableOffersInput);
        return VasOfferList;
    }
}
