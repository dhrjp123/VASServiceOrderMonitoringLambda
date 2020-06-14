package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewDataBuilderTest {
    final private static int NUMBER_OF_COLUMNS = 7;
    final private static String SKILL_TYPE = "Fan Installation";
    final private static String STORE_NAME = "Delhi";
    final private static String MARKETPLACE_ID = "India";
    private static final int TOTAL_CAPACITY = 23;
    private static final int AVAILABLE_CAPACITY = 14;
    private static final String CITY_VIEW_STORE_NAME = "Delhi";
    private static final String CITY_VIEW_ID = "AID3CMID1473001";
    private static final String ASIN = "AID3";
    private static final String CITY_VIEW_MERCHANT_ID = "CMID1";
    private static final String PINCODE = "473001";
    private static final String CITY_VIEW_STORE_ID = "CSID1";
    private static final String MERCHANT_VIEW_STORE_NAME = "Merchant3";
    private static final String MERCHANT_VIEW_ID = "AID3MID3473001";
    private static final String MERCHANT_VIEW_MERCHANT_ID = "MID3";
    private static final String MERCHANT_VIEW_STORE_ID = "SID3";
    @InjectMocks
    private ViewDataBuilder viewDataBuilder;
    @Mock
    private SPINServiceAccessor spinServiceAccessor;
    @Mock
    private VOSServiceAccessor vosServiceAccessor;
    @Mock
    private DynamoDbAccessor dynamoDbAccessor;
    @Mock
    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testViewDataBuilder_validCityViewInput_thenSuccessfulResponse() {
        ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO =
                getDefaultCityViewInput();
        ServiceCapacityTrackerComponentResponseBO expectedServiceCapacityTrackerComponentResponseBO =
                getDefaultCityViewOutput();
        Map<String, CityDetailsBO> cityMap = getDefaultCityMap();
        Map<String, String> asinMap = getDefaultAsinMap();
        List<Item> capacityDataItemList = getDefaultCapacityDataItemListForCityView();
        when(appConfig.getCityMapper()).thenReturn(cityMap);
        when(appConfig.getAsinMapper()).thenReturn(asinMap);
        when(dynamoDbAccessor.getCapacityData(anyListOf(DynamoDbAccessorInput.class)))
                .thenReturn(capacityDataItemList);
        ServiceCapacityTrackerComponentResponseBO serviceCapacityTrackerComponentResponseBO =
                viewDataBuilder.buildViewData(serviceCapacityTrackerComponentRequestBO);
        assertEquals(expectedServiceCapacityTrackerComponentResponseBO, serviceCapacityTrackerComponentResponseBO);
        verify(dynamoDbAccessor).getCapacityData(anyListOf(DynamoDbAccessorInput.class));
    }

    @Test
    public void testViewDataBuilder_validMerchantViewInput_thenSuccessfulResponse() {
        ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO =
                getDefaultMerchantViewInput();
        ServiceCapacityTrackerComponentResponseBO expectedServiceCapacityTrackerComponentResponseBO =
                getDefaultMerchantViewOutput();
        Map<String, CityDetailsBO> cityMap = getDefaultCityMap();
        Map<String, String> asinMap = getDefaultAsinMap();
        List<Item> capacityDataItemList = getDefaultCapacityDataItemListForMerchantView();
        List<VasOffer> vasOfferList = getDefaultVasOfferList();
        List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                getDefaultGetMerchantAggregatedDetailsOutputList();
        when(appConfig.getCityMapper()).thenReturn(cityMap);
        when(appConfig.getAsinMapper()).thenReturn(asinMap);
        when(dynamoDbAccessor.getCapacityData(anyListOf(DynamoDbAccessorInput.class)))
                .thenReturn(capacityDataItemList);
        when(vosServiceAccessor.getUnderlyingMerchantsId(any(GetBuyableOffersInput.class)))
                .thenReturn(vasOfferList);
        when(spinServiceAccessor.getMerchantNames(anyListOf(GetMerchantAggregatedDetailsInput.class)))
                .thenReturn(getMerchantAggregatedDetailsOutputList);
        ServiceCapacityTrackerComponentResponseBO serviceCapacityTrackerComponentResponseBO =
                viewDataBuilder.buildViewData(serviceCapacityTrackerComponentRequestBO);
        assertEquals(expectedServiceCapacityTrackerComponentResponseBO, serviceCapacityTrackerComponentResponseBO);
        verify(dynamoDbAccessor).getCapacityData(anyListOf(DynamoDbAccessorInput.class));
        verify(vosServiceAccessor).getUnderlyingMerchantsId(any(GetBuyableOffersInput.class));
        verify(spinServiceAccessor).getMerchantNames(anyListOf(GetMerchantAggregatedDetailsInput.class));
    }

    @Test(expected = NullPointerException.class)
    public void testViewDataBuilder_withNullInput() {
        viewDataBuilder.buildViewData(null);
    }

    public List<GetMerchantAggregatedDetailsOutput> getDefaultGetMerchantAggregatedDetailsOutputList() {
        MerchantAggregatedDetails merchantAggregatedDetails = MerchantAggregatedDetails.builder()
                .merchantName(MERCHANT_VIEW_STORE_NAME).build();
        GetMerchantAggregatedDetailsOutput getMerchantAggregatedDetailsOutput =
                GetMerchantAggregatedDetailsOutput.builder().merchantAggregatedDetails(merchantAggregatedDetails)
                        .build();
        return ImmutableList.of(getMerchantAggregatedDetailsOutput);
    }

    public List<VasOffer> getDefaultVasOfferList() {
        List<VasOffer> vasOfferList = new ArrayList<>();
        vasOfferList.add(VasOffer.builder().aggregated(true).merchantId("DummyMerchant").build());
        vasOfferList.add(VasOffer.builder().merchantId("MID3").aggregated(false).build());
        return vasOfferList;
    }

    public List<Item> getDefaultCapacityDataItemListForMerchantView() {
        List<Item> capacityDataItemList = new ArrayList<>();
        for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
            capacityDataItemList.add(new Item().withString("Id", MERCHANT_VIEW_ID).withString("Asin", ASIN)
                    .withString("Date", LocalDate.now().plusDays(date_idx).toString())
                    .withString("MerchantId", MERCHANT_VIEW_MERCHANT_ID).withString("PinCode", PINCODE)
                    .withString("StoreId", MERCHANT_VIEW_STORE_ID)
                    .withNumber("TotalCapacity", TOTAL_CAPACITY + date_idx)
                    .withNumber("AvailableCapacity", AVAILABLE_CAPACITY + date_idx));
        }
        return ImmutableList.copyOf(capacityDataItemList);
    }

    public List<Item> getDefaultCapacityDataItemListForCityView() {
        List<Item> capacityDataItemList = new ArrayList<>();
        for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
            capacityDataItemList.add(new Item().withString("Id", CITY_VIEW_ID).withString("Asin", ASIN)
                    .withString("Date", LocalDate.now().plusDays(date_idx).toString())
                    .withString("MerchantId", CITY_VIEW_MERCHANT_ID).withString("PinCode", PINCODE)
                    .withString("StoreId", CITY_VIEW_STORE_ID)
                    .withNumber("TotalCapacity", TOTAL_CAPACITY + date_idx)
                    .withNumber("AvailableCapacity", AVAILABLE_CAPACITY + date_idx));
        }
        return ImmutableList.copyOf(capacityDataItemList);
    }

    public Map<String, String> getDefaultAsinMap() {
        Map<String, String> asinMap = new HashMap<>();
        asinMap.put("Fan Installation", "AID3");
        return asinMap;
    }

    public Map<String, CityDetailsBO> getDefaultCityMap() {
        Map<String, CityDetailsBO> cityMap = new HashMap<>();
        cityMap.put("Delhi", CityDetailsBO.builder().merchantId("CMID1").pinCode("473001").build());
        return cityMap;
    }

    public ServiceCapacityTrackerComponentRequestBO getDefaultMerchantViewInput() {
        return ServiceCapacityTrackerComponentRequestBO.builder().skillType(SKILL_TYPE)
                .storeName(STORE_NAME).marketplaceId(MARKETPLACE_ID).build();
    }

    public ServiceCapacityTrackerComponentRequestBO getDefaultCityViewInput() {
        return ServiceCapacityTrackerComponentRequestBO.builder()
                .skillType(SKILL_TYPE).storeName("")
                .marketplaceId(MARKETPLACE_ID).build();
    }

    public ServiceCapacityTrackerComponentResponseBO getDefaultMerchantViewOutput() {
        List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
            capacityList.add(StoreCapacityBO.builder().availableCapacity(AVAILABLE_CAPACITY + date_idx)
                    .totalCapacity(TOTAL_CAPACITY + date_idx).build());
        }
        capacityList = ImmutableList.copyOf(capacityList);
        List<StoreCapacityDetailsBO> storeList = ImmutableList.of(StoreCapacityDetailsBO.builder()
                .capacityList(capacityList).merchantId(MERCHANT_VIEW_MERCHANT_ID)
                .storeName(MERCHANT_VIEW_STORE_NAME).build());
        return ServiceCapacityTrackerComponentResponseBO.builder().storeList(storeList).build();
    }

    public ServiceCapacityTrackerComponentResponseBO getDefaultCityViewOutput() {
        List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
            capacityList.add(StoreCapacityBO.builder().availableCapacity(AVAILABLE_CAPACITY + date_idx)
                    .totalCapacity(TOTAL_CAPACITY + date_idx).build());
        }
        capacityList = ImmutableList.copyOf(capacityList);
        List<StoreCapacityDetailsBO> storeList = ImmutableList.of(StoreCapacityDetailsBO.builder()
                .capacityList(capacityList).merchantId(CITY_VIEW_MERCHANT_ID).storeName(CITY_VIEW_STORE_NAME).build());
        return ServiceCapacityTrackerComponentResponseBO.builder().storeList(storeList).build();
    }
}