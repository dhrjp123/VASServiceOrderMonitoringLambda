package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazonaws.services.dynamodbv2.document.Item;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
public class ViewDataBuilder {
    final private static int NUMBER_OF_COLUMNS = 7;
    @Inject
    final private SPINServiceAccessor spinServiceAccessor;
    @Inject
    final private VOSServiceAccessor vosServiceAccessor;
    @Inject
    final private DynamoDbAccessor dynamoDbAccessor;
    @Inject
    final private AppConfig appConfig;

    public ServiceCapacityTrackerComponentResponseBO buildViewData(
            @NonNull final ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO) {
        List<MerchantDetailsBO> merchantList = getMerchantList(serviceCapacityTrackerComponentRequestBO);
        List<Item> capacityDataItemList = getStoreCapacityList(merchantList);
        return createResponse(merchantList, capacityDataItemList);
    }

    public ServiceCapacityTrackerComponentResponseBO createResponse(@NonNull final List<MerchantDetailsBO> merchantList,
                                                                    List<Item> capacityDataItemList) {
        Map<String, StoreCapacityBO> storeCapacityMap = new HashMap<>();
        for (Item item : capacityDataItemList) {
            storeCapacityMap.put(item.getString("Id") + item.getString("Date"),
                    StoreCapacityBO.builder().totalCapacity(item.getInt("TotalCapacity"))
                            .availableCapacity(item.getInt("AvailableCapacity")).build());
        }
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        for (MerchantDetailsBO merchantDetailsBO : merchantList) {
            LocalDate today = LocalDate.now();
            List<StoreCapacityBO> capacityList = new ArrayList<>();
            String Id =
                    merchantDetailsBO.getAsin() + merchantDetailsBO.getMerchantId() + merchantDetailsBO.getPinCode();
            for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                capacityList.add(storeCapacityMap.get(Id + today.plusDays(date_idx).toString()));
            }
            StoreCapacityDetailsBO storeCapacityDetailsBO = StoreCapacityDetailsBO.builder()
                    .storeName(merchantDetailsBO.getMerchantName())
                    .merchantId(merchantDetailsBO.getMerchantId())
                    .capacityList(capacityList).build();
            storeList.add(storeCapacityDetailsBO);
        }
        return ServiceCapacityTrackerComponentResponseBO.builder().storeList(storeList).build();
    }

    public List<Item> getStoreCapacityList(@NonNull final List<MerchantDetailsBO> merchantList) {
        return dynamoDbAccessor.getCapacityData(translateMerchantListTODynamoDbAccessorInputList(merchantList));
    }

    public List<DynamoDbAccessorInput> translateMerchantListTODynamoDbAccessorInputList(
            @NonNull final List<MerchantDetailsBO> merchantList) {
        List<DynamoDbAccessorInput> dynamoDbAccessorInputList = new ArrayList<>();
        for (MerchantDetailsBO merchantDetailsBO : merchantList)
            dynamoDbAccessorInputList.add(DynamoDbAccessorInput.builder()
                    .asin(merchantDetailsBO.getAsin())
                    .merchantId(merchantDetailsBO.getMerchantId())
                    .pinCode(merchantDetailsBO.getPinCode()).build());
        return dynamoDbAccessorInputList;
    }

    public String getAsinFromSkillType(@NonNull final String skillType) {
        Map<String, String> asinMap = appConfig.getAsinMapper();
        return asinMap.get(skillType);
    }

    public List<MerchantDetailsBO> getMerchantListForCityView(
            @NonNull final ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        String asin = getAsinFromSkillType(serviceCapacityTrackerComponentRequestBO.getSkillType());
        Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        Iterator<String> cityIterator = cityMap.keySet().iterator();
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
            @NonNull final ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO) {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        String asin = getAsinFromSkillType(serviceCapacityTrackerComponentRequestBO.getSkillType());
        Map<String, CityDetailsBO> cityMap = appConfig.getCityMapper();
        String pinCode = cityMap.get(serviceCapacityTrackerComponentRequestBO.getStoreName()).getPinCode();
        List<String> underlyingMerchantsId =
                getUnderlyingMerchantsId(asin, pinCode, serviceCapacityTrackerComponentRequestBO.getMarketplaceId());
        List<String> underlyingMerchantsName = getUnderlyingMerchantsName(underlyingMerchantsId
                , serviceCapacityTrackerComponentRequestBO.getMarketplaceId());
        for (int merchant_idx = 0; merchant_idx < underlyingMerchantsId.size(); merchant_idx++) {
            MerchantDetailsBO merchantDetailsBO = MerchantDetailsBO.builder()
                    .merchantName(underlyingMerchantsName.get(merchant_idx))
                    .merchantId(underlyingMerchantsId.get(merchant_idx))
                    .asin(asin).pinCode(pinCode).build();
            merchantList.add(merchantDetailsBO);
        }
        return merchantList;
    }

    public List<MerchantDetailsBO> getMerchantList(
            @NonNull final ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO) {
        List<MerchantDetailsBO> merchantList;
        if (serviceCapacityTrackerComponentRequestBO.getStoreName() == null ||
                serviceCapacityTrackerComponentRequestBO.getStoreName().equals(""))
            merchantList = getMerchantListForCityView(serviceCapacityTrackerComponentRequestBO);
        else
            merchantList = getMerchantListForMerchantView(serviceCapacityTrackerComponentRequestBO);
        return merchantList;
    }

    public List<String> getUnderlyingMerchantsName(@NonNull final List<String> underlyingMerchantsId,
                                                   String marketplaceId) {
        List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList = new ArrayList<>();
        for (String underlyingMerchantId : underlyingMerchantsId) {
            getMerchantAggregatedDetailsInputList.add(GetMerchantAggregatedDetailsInput.builder()
                    .encryptedMerchantId(underlyingMerchantId)
                    .encryptedMarketplaceId(marketplaceId).build());
        }
        List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                spinServiceAccessor.getMerchantNames(getMerchantAggregatedDetailsInputList);
        return getMerchantNamesFromGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsOutputList);
    }

    public List<String> getMerchantNamesFromGetMerchantAggregatedDetailsOutput
            (@NonNull final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList) {
        List<String> merchantNameList = new ArrayList<>();
        for (GetMerchantAggregatedDetailsOutput getMerchantAggregatedDetailsOutput :
                getMerchantAggregatedDetailsOutputList)
            merchantNameList.add(getMerchantAggregatedDetailsOutput.getMerchantAggregatedDetails().getMerchantName());
        return merchantNameList;
    }

    public List<String> getUnderlyingMerchantsId(@NonNull final String asin, @NonNull final String pinCode,
                                                 @NonNull final String marketplaceId) {
        AddressInput addressInput = AddressInput.builder().postalCode(pinCode).build();
        OfferSelector offerSelector = OfferSelector.builder().addressInput(addressInput).build();
        GetBuyableOffersInput getBuyableOffersInput = GetBuyableOffersInput.builder()
                .asin(asin).marketplaceId(marketplaceId).offerSelector(offerSelector).build();
        List<VasOffer> VasOfferList = vosServiceAccessor.getUnderlyingMerchantsId(getBuyableOffersInput);
        return getIndividualMerchantsId(VasOfferList);
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
