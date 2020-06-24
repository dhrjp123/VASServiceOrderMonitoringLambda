package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.ServiceCapacityDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class ServiceCapacityDetailsComponent {
    @NonNull
    private final MerchantDetailsBuilder merchantDetailsBuilder;
    @NonNull
    private final OfferDetailsBuilder offerDetailsBuilder;
    @NonNull
    private final ServiceCapacityDetailsBOBuilder serviceCapacityDetailsBOBuilder;
    @NonNull
    private final AppConfig appConfig;

    public ServiceCapacityDetailsBO trackCapacity
            (@NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityDetailsInputBO);
        final List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList =
                getCapacityDataBuilderInputList(
                        serviceCapacityDetailsInputBO, merchantList);
        return serviceCapacityDetailsBOBuilder.getResponse(serviceCapacityDetailsBOBuilderInputList,
                serviceCapacityDetailsInputBO.getNumberOfDays());
    }

    private String getAsinFromSkillType(final String skillType) {
        final Map<String, String> asinMap = appConfig.getAsinMapper();
        return asinMap.get(skillType);
    }

    private String getPinCodeFromStoreName(final String storeName) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        return cityMap.get(storeName).getPinCode();
    }

    private List<ServiceCapacityDetailsBOBuilderInput> getCapacityDataBuilderInputList(
            ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO
            , List<MerchantDetailsBO> merchantList) {
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList = new ArrayList<>();
        for (MerchantDetailsBO merchant : merchantList) {
            String pinCode;
            if (StringUtils.isNullOrEmpty(storeName))
                pinCode = getPinCodeFromStoreName(merchant.getMerchantName());
            else
                pinCode = getPinCodeFromStoreName(storeName);
            serviceCapacityDetailsBOBuilderInputList.add((ServiceCapacityDetailsBOBuilderInput.builder().asin(asin)
                    .pinCode(pinCode).merchantDetailsBO(merchant).build()));
        }
        return serviceCapacityDetailsBOBuilderInputList;
    }

    private List<MerchantDetailsBO> getMerchantList(
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        if (StringUtils.isNullOrEmpty(storeName))
            return getAggregatedMerchantDetailsList(cityMap);
        else {
            final String pinCode = cityMap.get(storeName).getPinCode();
            final List<OfferDetails> offerDetailsList =
                    offerDetailsBuilder
                            .getOfferDetailsList(serviceCapacityDetailsInputBO.getMarketplaceId(), asin, pinCode);
            final List<String> underlyingMerchantsId = getIndividualMerchantsId(offerDetailsList);
            return merchantDetailsBuilder.getMerchants(serviceCapacityDetailsInputBO.getMarketplaceId(),
                    underlyingMerchantsId);
        }
    }

    private List<MerchantDetailsBO> getAggregatedMerchantDetailsList(final Map<String, CityDetailsBO> cityMap) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        final Iterator<String> cityIterator = cityMap.keySet().iterator();
        while (cityIterator.hasNext()) {
            final String cityName = cityIterator.next();
            final CityDetailsBO cityDetailsBO = cityMap.get(cityName);
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
