package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.CapacityDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class ServiceCapacityDetailsComponent {
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
        final List<CapacityDataBuilderInput> capacityDataBuilderInputList = getCapacityDataBuilderInputList(
                serviceCapacityDetailsInputBO, merchantList);
        final Map<CapacityDataItemUniqueKeys, StoreCapacityBO> storeCapacityMap =
                capacityDataBuilder.getCapacityMap(capacityDataBuilderInputList
                        , serviceCapacityDetailsInputBO.getNumberOfDays());
        return createResponse(merchantList, storeCapacityMap, capacityDataBuilderInputList,
                serviceCapacityDetailsInputBO.getNumberOfDays());
    }

    private ServiceCapacityDetailsBO createResponse(final List<MerchantDetailsBO> merchantList,
                                                    final Map<CapacityDataItemUniqueKeys, StoreCapacityBO> storeCapacityMap,
                                                    final List<CapacityDataBuilderInput> capacityDataBuilderInputList,
                                                    int numberOfDays) {
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (int idx = 0; idx < merchantList.size(); idx++) {
            MerchantDetailsBO merchantDetailsBO = merchantList.get(idx);
            CapacityDataBuilderInput capacityDataBuilderInput = capacityDataBuilderInputList.get(idx);
            final LocalDate today = LocalDate.now();
            List<StoreCapacityBO> capacityList = new ArrayList<>();
            final String Id =
                    capacityDataBuilderInput.getAsin() + merchantDetailsBO.getMerchantId() +
                            capacityDataBuilderInput.getPinCode();
            for (int date_idx = 0; date_idx < numberOfDays; date_idx++) {
                capacityList.add(storeCapacityMap.get(
                        CapacityDataItemUniqueKeys.builder().Id(Id).date(today.plusDays(date_idx).toString()).build()));
            }
            StoreCapacityDetailsBO storeCapacityDetailsBO = StoreCapacityDetailsBO.builder()
                    .storeName(merchantDetailsBO.getMerchantName())
                    .merchantId(merchantDetailsBO.getMerchantId())
                    .capacityList(capacityList).build();
            storeList.add(storeCapacityDetailsBO);
        }
        Collections.sort(storeList, Comparator.comparing(StoreCapacityDetailsBO::getStoreName));
        return ServiceCapacityDetailsBO.builder().storeList(storeList).build();
    }

    private String getAsinFromSkillType(final String skillType) {
        final Map<String, String> asinMap = appConfig.getAsinMapper();
        return asinMap.get(skillType);
    }

    private String getPinCodeFromStoreName(final String storeName) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        return cityMap.get(storeName).getPinCode();
    }

    private List<CapacityDataBuilderInput> getCapacityDataBuilderInputList(
            ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO
            , List<MerchantDetailsBO> merchantList) {
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        List<CapacityDataBuilderInput> capacityDataBuilderInputList = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(storeName)) {
            for (MerchantDetailsBO merchant : merchantList)
                capacityDataBuilderInputList.add(CapacityDataBuilderInput.builder().merchantId(merchant.getMerchantId())
                        .asin(asin).pinCode(getPinCodeFromStoreName(merchant.getMerchantName())).build());
        } else {
            final String pinCode = getPinCodeFromStoreName(storeName);
            for (MerchantDetailsBO merchant : merchantList)
                capacityDataBuilderInputList.add(CapacityDataBuilderInput.builder().merchantId(merchant.getMerchantId())
                        .asin(asin).pinCode(pinCode).build());
        }
        return capacityDataBuilderInputList;
    }

    private List<MerchantDetailsBO> getMerchantList(
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        String storeName = serviceCapacityDetailsInputBO.getStoreName();
        String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        if (StringUtils.isNullOrEmpty(storeName))
            return getAggregatedMerchantDetailsList(cityMap, asin);
        else {
            final String pinCode = cityMap.get(storeName).getPinCode();
            final List<OfferDetails> offerDetailsList =
                    offerDetailsBuilder
                            .getOfferDetailsList(asin, pinCode, serviceCapacityDetailsInputBO.getMarketplaceId());
            final List<String> underlyingMerchantsId = getIndividualMerchantsId(offerDetailsList);
            return merchantDetailsBuilder.getMerchants(serviceCapacityDetailsInputBO.getMarketplaceId(),
                    underlyingMerchantsId);
        }
    }

    private List<MerchantDetailsBO> getAggregatedMerchantDetailsList(final Map<String, CityDetailsBO> cityMap,
                                                                     String asin) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        final Iterator<String> cityIterator = cityMap.keySet().iterator();
        while (cityIterator.hasNext()) {
            String cityName = cityIterator.next();
            CityDetailsBO cityDetailsBO = cityMap.get(cityName);
            merchantList.add(MerchantDetailsBO.builder().merchantName(cityName)
                    .merchantId(cityDetailsBO.getMerchantId()).build());
        }
        return merchantList;
    }

    private List<String> getIndividualMerchantsId(final List<OfferDetails> offerDetailsList) {
        List<String> individualMerchantsIdList = new ArrayList<>();
        for (OfferDetails offerDetails : offerDetailsList) {
            if (!offerDetails.isAggregated())
                individualMerchantsIdList.add(offerDetails.getMerchantId());
        }
        return individualMerchantsIdList;
    }
}
