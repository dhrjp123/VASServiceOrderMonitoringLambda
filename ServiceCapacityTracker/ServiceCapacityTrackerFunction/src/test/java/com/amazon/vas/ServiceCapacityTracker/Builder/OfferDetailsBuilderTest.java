package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OfferDetailsBuilderTest {
    @InjectMocks
    private OfferDetailsBuilder offerDetailsBuilder;
    @Mock
    private VOSServiceAccessor vosServiceAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOfferDetailsList_whenValidInputIsPassed_thenSuccessfulResponse() {
        final GetBuyableOffersInput getBuyableOffersInput = getDefaultGetBuyableOffersInput();
        final List<VasOffer> vasOfferList = getDefaultVasOfferList();
        final List<OfferDetails> expectedOfferDetailsList = getDefaultOfferDetailsList();
        Mockito.when(vosServiceAccessor.getVasOffers(getBuyableOffersInput)).thenReturn(vasOfferList);
        final List<OfferDetails> offerDetailsList = offerDetailsBuilder.getOfferDetailsList(ConstantsClass.ASIN,
                ConstantsClass.PINCODE, ConstantsClass.MARKETPLACE_ID);
        assertEquals(expectedOfferDetailsList, offerDetailsList);
        Mockito.verify(vosServiceAccessor).getVasOffers(getBuyableOffersInput);
    }

    private GetBuyableOffersInput getDefaultGetBuyableOffersInput() {
        final AddressInput addressInput = AddressInput.builder().postalCode(ConstantsClass.PINCODE).build();
        final OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        return GetBuyableOffersInput.builder().asin(ConstantsClass.ASIN).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .offerSelector(offerSelector).build();
    }

    private List<VasOffer> getDefaultVasOfferList() {
        List<VasOffer> vasOfferList = new ArrayList<>();
        vasOfferList
                .add(VasOffer.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID).aggregated(false).build());
        vasOfferList.add(VasOffer.builder().merchantId(ConstantsClass.DUMMY_MERCHANT).aggregated(true).build());
        return vasOfferList;
    }

    private List<OfferDetails> getDefaultOfferDetailsList() {
        List<OfferDetails> offerDetailsList = new ArrayList<>();
        offerDetailsList.add(OfferDetails.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                .isAggregated(false).build());
        offerDetailsList.add(OfferDetails.builder().merchantId(ConstantsClass.DUMMY_MERCHANT)
                .isAggregated(true).build());
        return offerDetailsList;
    }
}