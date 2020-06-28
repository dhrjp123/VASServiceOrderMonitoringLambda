package com.amazon.vas.servicecapacitytracker.accessor;

import com.amazon.vas.servicecapacitytracker.model.vosservicemodel.GetBuyableOffersInput;
import com.amazon.vas.servicecapacitytracker.model.vosservicemodel.VasOffer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class VOSServiceAccessor {
    public List<VasOffer> getVasOffers(@NonNull final GetBuyableOffersInput getBuyableOffersInput) {
        // TODO: Call VOS Service Client instead of returning Dummy Values
        return getVasOfferList(getBuyableOffersInput);
    }

    //Dummy Function to provide some Dummy Data
    private List<VasOffer> getVasOfferList(final GetBuyableOffersInput getBuyableOffersInput) {
        final List<VasOffer> vasOfferList = new ArrayList<>();
        vasOfferList.add(VasOffer.builder().aggregated(true).merchantId("DummyMerchant").build());
        final Map<String, VasOffer> dummyDataMap = getDummyDataMap();
        final String asin = getBuyableOffersInput.getAsin();
        final String pinCode = getBuyableOffersInput.getOfferSelector().getAddressInput().getPostalCode();
        vasOfferList.add(dummyDataMap.get(asin + "_" + pinCode));
        return vasOfferList;
    }

    //Dummy Function
    private Map<String, VasOffer> getDummyDataMap() {
        final Map<String, VasOffer> dummyDataMap = new HashMap<>();
        dummyDataMap.put("AID1_473001", VasOffer.builder().merchantId("MID1").aggregated(false).build());
        dummyDataMap.put("AID1_462003", VasOffer.builder().merchantId("MID4").aggregated(false).build());
        dummyDataMap.put("AID2_473001", VasOffer.builder().merchantId("MID2").aggregated(false).build());
        dummyDataMap.put("AID2_462003", VasOffer.builder().merchantId("MID5").aggregated(false).build());
        dummyDataMap.put("AID3_473001", VasOffer.builder().merchantId("MID3").aggregated(false).build());
        dummyDataMap.put("AID3_462003", VasOffer.builder().merchantId("MID6").aggregated(false).build());
        return dummyDataMap;
    }
}
