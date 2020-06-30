package com.amazon.vas.servicecapacitytracker.builder;

import com.amazon.vas.servicecapacitytracker.accessor.VOSServiceAccessor;
import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.bo.OfferDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.vos.AddressInput;
import com.amazon.vas.servicecapacitytracker.model.vos.GetBuyableOffersInput;
import com.amazon.vas.servicecapacitytracker.model.vos.OfferSelector;
import com.amazon.vas.servicecapacitytracker.model.vos.VasOffer;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OfferDetailsBOBuilderTest {
    @InjectMocks
    private OfferDetailsBOBuilder offerDetailsBOBuilder;
    @Mock
    private VOSServiceAccessor vosServiceAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOfferDetailsList_whenValidInputIsPassed_thenSuccessfulResponse() {
        final GetBuyableOffersInput getBuyableOffersInput = getDefaultGetBuyableOffersInput();
        final List<VasOffer> vasOfferList = ImmutableList.of(VasOffer.builder()
                        .merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).aggregated(false).build(),
                VasOffer.builder().merchantId(ConstantsClass.DUMMY_MERCHANT).aggregated(true).build());
        final List<OfferDetailsBO> expectedOfferDetailsBOList = ImmutableList.of(OfferDetailsBO.builder()
                        .merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).isAggregated(false).build(),
                OfferDetailsBO.builder().merchantId(ConstantsClass.DUMMY_MERCHANT).isAggregated(true).build());

        Mockito.when(vosServiceAccessor.getVasOffers(getBuyableOffersInput)).thenReturn(vasOfferList);

        final List<OfferDetailsBO> offerDetailsBOList = offerDetailsBOBuilder.getOfferDetailsBOList(
                ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE);

        assertEquals(expectedOfferDetailsBOList, offerDetailsBOList);
        Mockito.verify(vosServiceAccessor).getVasOffers(getBuyableOffersInput);
    }

    private GetBuyableOffersInput getDefaultGetBuyableOffersInput() {
        final AddressInput addressInput = AddressInput.builder().postalCode(ConstantsClass.PINCODE).build();
        final OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        return GetBuyableOffersInput.builder().asin(ConstantsClass.ASIN).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .offerSelector(offerSelector).build();
    }
}