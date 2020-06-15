package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.CapacityDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.AllArgsConstructor;
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
    private static final int NUMBER_OF_COLUMNS=7;
    @NonNull
    private final MerchantDetailsBuilder merchantDetailsBuilder;
    @NonNull
    private final OfferDetailsBuilder offerDetailsBuilder;
    @NonNull
    private final CapacityDataBuilder capacityDataBuilder;
    @NonNull
    private final AppConfig appConfig;
    public ServiceCapacityDetailsBO trackCapacity
            (@NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO)
    {
        final List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityDetailsInputBO);
        final Map<DynamoDbTableKeys,StoreCapacityBO>storeCapacityMap=capacityDataBuilder.getCapacityMap(merchantList);
        return createResponse(merchantList,storeCapacityMap);
    }
    public ServiceCapacityDetailsBO createResponse(@NonNull final List<MerchantDetailsBO> merchantList, @NonNull final Map<DynamoDbTableKeys,StoreCapacityBO>storeCapacityMap)
    {
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (MerchantDetailsBO merchantDetailsBO : merchantList) {
            final LocalDate today = LocalDate.now();
            List<StoreCapacityBO> capacityList = new ArrayList<>();
            final String Id =
                    merchantDetailsBO.getAsin() + merchantDetailsBO.getMerchantId() + merchantDetailsBO.getPinCode();
            for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                capacityList.add(storeCapacityMap.get(DynamoDbTableKeys.builder().Id(Id).date(today.plusDays(date_idx).toString()).build()));
            }
            StoreCapacityDetailsBO storeCapacityDetailsBO = StoreCapacityDetailsBO.builder()
                    .storeName(merchantDetailsBO.getMerchantName())
                    .merchantId(merchantDetailsBO.getMerchantId())
                    .capacityList(capacityList).build();
            storeList.add(storeCapacityDetailsBO);
        }
        return ServiceCapacityDetailsBO.builder().storeList(storeList).build();
    }
    public String getAsinFromSkillType(@NonNull final String skillType)
    {
        final Map<String, String> asinMap = appConfig.getAsinMapper();
        return asinMap.get(skillType);
    }
    public List<MerchantDetailsBO> getMerchantList(
            @NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        List<MerchantDetailsBO> merchantList;
        if (serviceCapacityDetailsInputBO.getStoreName() == null ||
                serviceCapacityDetailsInputBO.getStoreName().equals(""))
            merchantList = getMerchantListForCityView(serviceCapacityDetailsInputBO);
        else
            merchantList = getMerchantListForMerchantView(serviceCapacityDetailsInputBO);
        return merchantList;
    }
    public List<MerchantDetailsBO> getMerchantListForCityView(
            @NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        final Iterator<String> cityIterator = cityMap.keySet().iterator();
        while (cityIterator.hasNext()) {
            String cityName = cityIterator.next();
            CityDetailsBO cityDetailsBO = cityMap.get(cityName);
            MerchantDetailsBO merchantDetailsBO = MerchantDetailsBO.builder().merchantName(cityName)
                    .merchantId(cityDetailsBO.getMerchantId())
                    .pinCode(cityDetailsBO.getPinCode())
                    .asin(asin).build();
            merchantList.add(merchantDetailsBO);
        }
        return merchantList;
    }
    public List<MerchantDetailsBO> getMerchantListForMerchantView(
            @NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        List<MerchantDetailsBO> merchantList;
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        final String pinCode = cityMap.get(serviceCapacityDetailsInputBO.getStoreName()).getPinCode();
        final List<VasOffer> vasOfferList =
                offerDetailsBuilder.getVasOfferList(OfferDetailsBuilderInput.builder().asin(asin)
                        .marketplaceId(serviceCapacityDetailsInputBO.getMarketplaceId())
                        .pinCode(pinCode).build());
        final List<String> underlyingMerchantsId=getIndividualMerchantsId(vasOfferList);
        merchantList=merchantDetailsBuilder.getUnderlyingMerchants(MerchantDetailsBuilderInput.builder()
                .asin(asin).pinCode(pinCode).underlyingMerchantsId(underlyingMerchantsId)
                .marketplaceId(serviceCapacityDetailsInputBO.getMarketplaceId()).build());
        return merchantList;
    }
    public List<String> getIndividualMerchantsId(@NonNull final List<VasOffer> VasOfferList) {
        List<String> individualMerchantIdList = new ArrayList<>();
        for (VasOffer vasOffer : VasOfferList) {
            if (!vasOffer.isAggregated())
                individualMerchantIdList.add(vasOffer.getMerchantId());
        }
        return individualMerchantIdList;
    }
}
