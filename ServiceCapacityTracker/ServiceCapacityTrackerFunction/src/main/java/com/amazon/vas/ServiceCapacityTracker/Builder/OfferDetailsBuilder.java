package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class OfferDetailsBuilder {
    @NonNull
    private final VOSServiceAccessor vosServiceAccessor;

    public List<OfferDetails> getOfferDetailsList(@NonNull final String asin, @NonNull final String pinCode,
                                                  @NonNull final String marketplaceId) {
        final AddressInput addressInput = AddressInput.builder().postalCode(pinCode).build();
        final OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        final GetBuyableOffersInput getBuyableOffersInput = GetBuyableOffersInput.builder()
                .asin(asin).marketplaceId(marketplaceId)
                .offerSelector(offerSelector).build();
        final List<VasOffer> VasOfferList = vosServiceAccessor.getVasOffers(getBuyableOffersInput);
        return translateToOfferDetailsList(VasOfferList);
    }

    private List<OfferDetails> translateToOfferDetailsList(final List<VasOffer> vasOfferList) {
        List<OfferDetails> offerDetailsList = new ArrayList<>();
        for (VasOffer vasOffer : vasOfferList) {
            if (!vasOffer.isAggregated())
                offerDetailsList.add(OfferDetails.builder().merchantId(vasOffer.getMerchantId()).build());
        }
        return offerDetailsList;
    }
}
