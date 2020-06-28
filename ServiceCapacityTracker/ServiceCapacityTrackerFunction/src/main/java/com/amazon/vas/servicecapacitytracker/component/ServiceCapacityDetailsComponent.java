package com.amazon.vas.servicecapacitytracker.component;

import com.amazon.vas.servicecapacitytracker.builder.MerchantDetailsBuilder;
import com.amazon.vas.servicecapacitytracker.builder.OfferDetailsBuilder;
import com.amazon.vas.servicecapacitytracker.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.config.AppConfig;
import com.amazon.vas.servicecapacitytracker.model.*;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class ServiceCapacityDetailsComponent {
    @NonNull
    private final MerchantDetailsBuilder merchantDetailsBuilder;
    @NonNull
    private final OfferDetailsBuilder offerDetailsBuilder;
    @NonNull
    private final StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @NonNull
    private final AppConfig appConfig;

    public List<StoreCapacityDetailsBO> getStoreCapacityDetails
            (@NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityDetailsInputBO);
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                getStoreCapacityDetailsBOBuilderInputList(serviceCapacityDetailsInputBO, merchantList);
        return storeCapacityDetailsBOBuilder.getResponse(storeCapacityDetailsBOBuilderInputList,
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

    private List<StoreCapacityDetailsBOBuilderInput> getStoreCapacityDetailsBOBuilderInputList(
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO
            , final List<MerchantDetailsBO> merchantList) {
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        return merchantList.stream()
                .map(merchant -> {
                    String pinCode;
                    if (StringUtils.isNullOrEmpty(storeName))
                        pinCode = getPinCodeFromStoreName(merchant.getMerchantName());
                    else
                        pinCode = getPinCodeFromStoreName(storeName);
                    return StoreCapacityDetailsBOBuilderInput.builder().merchantDetailsBO(merchant)
                            .asin(asin).pinCode(pinCode).build();
                }).collect(Collectors.toList());
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
            final List<String> underlyingMerchantsId = offerDetailsList.stream().flatMap(offerDetails -> {
                if (!offerDetails.isAggregated())
                    return Stream.of(offerDetails.getMerchantId());
                else
                    return Stream.empty();
            }).collect(Collectors.toList());
            return merchantDetailsBuilder.getMerchants(serviceCapacityDetailsInputBO.getMarketplaceId(),
                    underlyingMerchantsId);
        }
    }

    private List<MerchantDetailsBO> getAggregatedMerchantDetailsList(final Map<String, CityDetailsBO> cityMap) {
        final List<String> cityList = new ArrayList<>(cityMap.keySet());
        return cityList.stream().map(city -> {
            CityDetailsBO cityDetailsBO = cityMap.get(city);
            return MerchantDetailsBO.builder().merchantName(city).merchantId(cityDetailsBO.getMerchantId()).build();
        }).collect(Collectors.toList());
    }
}
