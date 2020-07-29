package accessor;

import model.vos.GetBuyableOffersInput;
import model.vos.VasOffer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

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
        final String asin = getBuyableOffersInput.getAsin();
        final String pinCode = getBuyableOffersInput.getOfferSelector().getAddressInput().getPostalCode();
        vasOfferList.addAll(getDummyDataMap(asin, pinCode));
        return vasOfferList;
    }

    //Dummy Function
    private List<VasOffer> getDummyDataMap(String asin, String pinCode) {
        final Map<String, VasOffer> dummyDataMap = new HashMap<>();
        Random random = new Random(0);
        List<String> asins = ImmutableList.of("AID1", "AID2" , "AID3");
        List<String> pins = ImmutableList.of("473001", "462003");

        List<VasOffer> vasOffers = new ArrayList<>();
        for( int i = 0 ; i< 100 ; i++ ){
            String asinX = asins.get(random.nextInt(3));
            String pinX = pins.get(random.nextInt(2));
            String mid = "MID"+random.nextInt(10);

            if(asin.equalsIgnoreCase(asinX) && pinCode.equalsIgnoreCase(pinX))
                vasOffers.add(VasOffer.builder().merchantId(mid).aggregated(false).build());
        }
//        dummyDataMap.put("AID1_473001", VasOffer.builder().merchantId("MID1").aggregated(false).build());
//        dummyDataMap.put("AID1_462003", VasOffer.builder().merchantId("MID4").aggregated(false).build());
//        dummyDataMap.put("AID2_473001", VasOffer.builder().merchantId("MID2").aggregated(false).build());
//        dummyDataMap.put("AID2_462003", VasOffer.builder().merchantId("MID5").aggregated(false).build());
//        dummyDataMap.put("AID3_473001", VasOffer.builder().merchantId("MID3").aggregated(false).build());
//        dummyDataMap.put("AID3_462003", VasOffer.builder().merchantId("MID6").aggregated(false).build());
        Set<String> set = new HashSet<>(vasOffers.size());
        return vasOffers.stream().filter(p -> set.add(p.getMerchantId())).collect(Collectors.toList());
    }
}
