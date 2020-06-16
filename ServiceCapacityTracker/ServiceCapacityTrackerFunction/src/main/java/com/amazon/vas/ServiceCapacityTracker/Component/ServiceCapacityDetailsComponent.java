package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.CapacityDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class ServiceCapacityDetailsComponent {
    private static final int NUMBER_OF_COLUMNS = 7;
    @NonNull
    private final MerchantDetailsBuilder merchantDetailsBuilder;
    @NonNull
    private final OfferDetailsBuilder offerDetailsBuilder;
    @NonNull
    private final CapacityDataBuilder capacityDataBuilder;
    @NonNull
    private final AppConfig appConfig;

    public ServiceCapacityDetailsBO trackCapacity
            (@NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityDetailsInputBO);
        final Map<MerchantUniqueKeysBO, StoreCapacityBO> storeCapacityMap =
                capacityDataBuilder.getCapacityMap(merchantList);
        return createResponse(merchantList, storeCapacityMap);
    }

    private ServiceCapacityDetailsBO createResponse(final List<MerchantDetailsBO> merchantList,
                                                    final Map<MerchantUniqueKeysBO, StoreCapacityBO> storeCapacityMap) {
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (MerchantDetailsBO merchantDetailsBO : merchantList) {
            final LocalDate today = LocalDate.now();
            List<StoreCapacityBO> capacityList = new ArrayList<>();
            final String Id =
                    merchantDetailsBO.getAsin() + merchantDetailsBO.getMerchantId() + merchantDetailsBO.getPinCode();
            for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                capacityList.add(storeCapacityMap.get(
                        MerchantUniqueKeysBO.builder().Id(Id).date(today.plusDays(date_idx).toString()).build()));
            }
            StoreCapacityDetailsBO storeCapacityDetailsBO = StoreCapacityDetailsBO.builder()
                    .storeName(merchantDetailsBO.getMerchantName())
                    .merchantId(merchantDetailsBO.getMerchantId())
                    .capacityList(capacityList).build();
            storeList.add(storeCapacityDetailsBO);
        }
        return ServiceCapacityDetailsBO.builder().storeList(storeList).build();
    }

    private String getAsinFromSkillType(final String skillType) {
        final Map<String, String> asinMap = appConfig.getAsinMapper();
        return asinMap.get(skillType);
    }

    private List<MerchantDetailsBO> getMerchantList(
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        String storeName = serviceCapacityDetailsInputBO.getStoreName();
        String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        if (storeName == null || storeName.equals(""))
            return getMerchantListFromCityMap(cityMap, asin);
        else {
            final String pinCode = cityMap.get(storeName).getPinCode();
            final List<OfferDetails> offerDetailsList =
                    offerDetailsBuilder
                            .getOfferDetailsList(asin, pinCode, serviceCapacityDetailsInputBO.getMarketplaceId());
            final List<String> underlyingMerchantsId = getIndividualMerchantsId(offerDetailsList);
            return merchantDetailsBuilder.getMerchants(asin, pinCode,
                    serviceCapacityDetailsInputBO.getMarketplaceId(), underlyingMerchantsId);
        }
    }

    private List<MerchantDetailsBO> getMerchantListFromCityMap(final Map<String, CityDetailsBO> cityMap, String asin) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        final Iterator<String> cityIterator = cityMap.keySet().iterator();
        while (cityIterator.hasNext()) {
            String cityName = cityIterator.next();
            CityDetailsBO cityDetailsBO = cityMap.get(cityName);
            merchantList.add(MerchantDetailsBO.builder().merchantName(cityName).asin(asin)
                    .merchantId(cityDetailsBO.getMerchantId()).pinCode(cityDetailsBO.getPinCode()).build());
        }
        return merchantList;
    }

    private List<String> getIndividualMerchantsId(final List<OfferDetails> offerDetailsList) {
        List<String> individualMerchantsIdList = new ArrayList<>();
        for (OfferDetails offerDetails : offerDetailsList)
            individualMerchantsIdList.add(offerDetails.getMerchantId());
        return individualMerchantsIdList;
    }
}
