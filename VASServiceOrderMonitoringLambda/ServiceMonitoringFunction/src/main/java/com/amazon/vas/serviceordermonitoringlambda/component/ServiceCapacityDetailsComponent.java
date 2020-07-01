package com.amazon.vas.serviceordermonitoringlambda.component;

import com.amazon.vas.serviceordermonitoringlambda.builder.MerchantDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.OfferDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.config.AppConfig;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.CityDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.MerchantDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.OfferDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.ServiceCapacityDetailsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBOBuilderInput;
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
    private final OfferDetailsBOBuilder offerDetailsBOBuilder;
    @NonNull
    private final StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @NonNull
    private final AppConfig appConfig;

    public List<StoreCapacityDetailsBO> getStoreCapacityDetails(
            @NonNull final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityDetailsInputBO);
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                getStoreCapacityDetailsBOBuilderInputList(serviceCapacityDetailsInputBO, merchantList);
        return storeCapacityDetailsBOBuilder.getResponse(serviceCapacityDetailsInputBO.getMarketplaceId(),
                storeCapacityDetailsBOBuilderInputList, serviceCapacityDetailsInputBO.getNumberOfDays());
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
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO,
            final List<MerchantDetailsBO> merchantList) {
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        return merchantList.stream()
                .map(merchant -> {
                    String pinCode;
                    if (StringUtils.isNullOrEmpty(storeName)) {
                        pinCode = getPinCodeFromStoreName(merchant.getMerchantName());
                    } else {
                        pinCode = getPinCodeFromStoreName(storeName);
                    }
                    return StoreCapacityDetailsBOBuilderInput.builder().merchantDetailsBO(merchant)
                            .asin(asin).pinCode(pinCode).build();
                }).collect(Collectors.toList());
    }

    private List<MerchantDetailsBO> getMerchantList(
            final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO) {
        final Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        final String storeName = serviceCapacityDetailsInputBO.getStoreName();
        final String asin = getAsinFromSkillType(serviceCapacityDetailsInputBO.getSkillType());
        if (StringUtils.isNullOrEmpty(storeName)) {
            return getAggregatedMerchantDetailsList(cityMap);
        } else {
            final String pinCode = cityMap.get(storeName).getPinCode();
            final List<OfferDetailsBO> offerDetailsBOList =
                    offerDetailsBOBuilder
                            .getOfferDetailsBOList(serviceCapacityDetailsInputBO.getMarketplaceId(), asin, pinCode);
            final List<String> underlyingMerchantsId = offerDetailsBOList.stream().flatMap(offerDetailsBO -> {
                if (!offerDetailsBO.isAggregated()) {
                    return Stream.of(offerDetailsBO.getMerchantId());
                } else {
                    return Stream.empty();
                }
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
