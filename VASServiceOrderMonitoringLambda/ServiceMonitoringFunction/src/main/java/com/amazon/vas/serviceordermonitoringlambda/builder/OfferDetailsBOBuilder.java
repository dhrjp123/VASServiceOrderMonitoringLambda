package com.amazon.vas.serviceordermonitoringlambda.builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.VOSServiceAccessor;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.OfferDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.vos.AddressInput;
import com.amazon.vas.serviceordermonitoringlambda.model.vos.GetBuyableOffersInput;
import com.amazon.vas.serviceordermonitoringlambda.model.vos.OfferSelector;
import com.amazon.vas.serviceordermonitoringlambda.model.vos.VasOffer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class OfferDetailsBOBuilder {
    @NonNull
    private final VOSServiceAccessor vosServiceAccessor;

    public List<OfferDetailsBO> getOfferDetailsBOList(@NonNull final String marketplaceId, @NonNull final String asin,
                                                      @NonNull final String pinCode) {
        final AddressInput addressInput = AddressInput.builder().postalCode(pinCode).build();
        final OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        final GetBuyableOffersInput getBuyableOffersInput = GetBuyableOffersInput.builder()
                .asin(asin).marketplaceId(marketplaceId)
                .offerSelector(offerSelector).build();
        final List<VasOffer> vasOfferList = vosServiceAccessor.getVasOffers(getBuyableOffersInput);
        return vasOfferList.stream().map(vasOffer -> OfferDetailsBO.builder()
                .merchantId(vasOffer.getMerchantId()).isAggregated(vasOffer.isAggregated()).build())
                .collect(Collectors.toList());
    }
}
