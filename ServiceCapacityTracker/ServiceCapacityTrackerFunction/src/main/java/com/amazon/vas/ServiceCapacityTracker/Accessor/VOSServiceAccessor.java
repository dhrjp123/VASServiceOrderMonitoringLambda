package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Model.GetBuyableOffersInput;
import com.amazon.vas.ServiceCapacityTracker.Model.VasOffer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class VOSServiceAccessor {
    //Dummy Function to provide some Dummy Data
    private List<VasOffer> getVasOfferList(@NonNull final GetBuyableOffersInput getBuyableOffersInput) {
        List<VasOffer> VasOfferList = new ArrayList<>();
        VasOfferList.add(VasOffer.builder().aggregated(true).merchantId("DummyMerchant").build());
        String getBuyableOffersInputPinCode =
                getBuyableOffersInput.getOfferSelector().getAddressInput().getPostalCode();
        if (getBuyableOffersInput.getAsin().equals("AID1")) {
            if (getBuyableOffersInputPinCode.equals("473001"))
                VasOfferList.add(VasOffer.builder().merchantId("MID1").aggregated(false).build());
            else
                VasOfferList.add(VasOffer.builder().merchantId("MID4").aggregated(false).build());
        } else if (getBuyableOffersInput.getAsin().equals("AID2")) {
            if (getBuyableOffersInputPinCode.equals("473001"))
                VasOfferList.add(VasOffer.builder().merchantId("MID2").aggregated(false).build());
            else
                VasOfferList.add(VasOffer.builder().merchantId("MID5").aggregated(false).build());
        } else {
            if (getBuyableOffersInputPinCode.equals("473001"))
                VasOfferList.add(VasOffer.builder().merchantId("MID3").aggregated(false).build());
            else
                VasOfferList.add(VasOffer.builder().merchantId("MID6").aggregated(false).build());
        }
        return VasOfferList;
    }

    public List<VasOffer> getUnderlyingMerchantsId(@NonNull final GetBuyableOffersInput getBuyableOffersInput) {
        //We can call the VOS service from here. Currently calling the dummy function.
        return getVasOfferList(getBuyableOffersInput);
    }
}
