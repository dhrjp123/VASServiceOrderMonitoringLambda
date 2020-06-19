package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.CapacityDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ServiceCapacityDetailsComponentTest {
    @InjectMocks
    private ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;
    @Mock
    private MerchantDetailsBuilder merchantDetailsBuilder;
    @Mock
    private OfferDetailsBuilder offerDetailsBuilder;
    @Mock
    private CapacityDataBuilder capacityDataBuilder;
    @Mock
    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTrackCapacity_whenValidInputIsPassedWithEmptyStoreName_thenSuccessfulResponse() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new ServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build();
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(capacityDataBuilder.getCapacityMap(getDefaultCapacityDataBuilderInputListForAggregatedMerchants(),
                ConstantsClass.NUMBER_OF_COLUMNS)).thenReturn(getDefaultCapacityMapForAggregatedMerchants());
        ServiceCapacityDetailsBO expectedServiceCapacityDetailsBO = new ServiceCapacityDetailsBOBuilder()
                .forAggregatedMerchants().build();
        ServiceCapacityDetailsBO serviceCapacityDetailsBO = serviceCapacityDetailsComponent
                .trackCapacity(serviceCapacityDetailsInputBO);
        assertEquals(expectedServiceCapacityDetailsBO, serviceCapacityDetailsBO);
        Mockito.verify(capacityDataBuilder)
                .getCapacityMap(getDefaultCapacityDataBuilderInputListForAggregatedMerchants(),
                        ConstantsClass.NUMBER_OF_COLUMNS);
    }

    @Test
    public void testTrackCapacity_whenValidInputIsPassedWithStoreName_thenSuccessfulResponse() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new ServiceCapacityDetailsInputBOBuilder().withStoreName().build();
        final ServiceCapacityDetailsBO expectedServiceCapacityDetailsBO =
                new ServiceCapacityDetailsBOBuilder().forIndividualMerchants().build();
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(capacityDataBuilder.getCapacityMap(getDefaultCapacityDataBuilderInputListForIndividualMerchants(),
                ConstantsClass.NUMBER_OF_COLUMNS)).thenReturn(getDefaultCapacityMapForIndividualMerchants());
        Mockito.when(offerDetailsBuilder.getOfferDetailsList(ConstantsClass.ASIN, ConstantsClass.PINCODE,
                ConstantsClass.MARKETPLACE_ID)).thenReturn(getDefaultOfferDetailsList());
        Mockito.when(merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                getDefaultUnderlyingMerchantsIDList()))
                .thenReturn(getDefaultMerchantListForIndividualMerchants());
        final ServiceCapacityDetailsBO serviceCapacityDetailsBO = serviceCapacityDetailsComponent
                .trackCapacity(serviceCapacityDetailsInputBO);
        assertEquals(expectedServiceCapacityDetailsBO, serviceCapacityDetailsBO);
        Mockito.verify(capacityDataBuilder)
                .getCapacityMap(getDefaultCapacityDataBuilderInputListForIndividualMerchants(),
                        ConstantsClass.NUMBER_OF_COLUMNS);
        Mockito.verify(offerDetailsBuilder).getOfferDetailsList(ConstantsClass.ASIN, ConstantsClass.PINCODE,
                ConstantsClass.MARKETPLACE_ID);
        Mockito.verify(merchantDetailsBuilder).getMerchants(ConstantsClass.MARKETPLACE_ID,
                getDefaultUnderlyingMerchantsIDList());
    }

    private Map<String, String> getDefaultAsinMap() {
        Map<String, String> asinMap = new HashMap<>();
        asinMap.put(ConstantsClass.SKILL_TYPE, ConstantsClass.ASIN);
        return asinMap;
    }

    private Map<String, CityDetailsBO> getDefaultCityMap() {
        Map<String, CityDetailsBO> cityMap = new HashMap<>();
        cityMap.put(ConstantsClass.AGGREGATED_MERCHANT_NAME, CityDetailsBO.builder().pinCode(ConstantsClass.PINCODE)
                .merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID).build());
        return cityMap;
    }

    private Map<CapacityDataItemUniqueKeys, StoreCapacityBO> getDefaultCapacityMapForAggregatedMerchants() {
        Map<CapacityDataItemUniqueKeys, StoreCapacityBO> capacityMap = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityMap.put(new MerchantUniqueKeysBOBuilder()
                            .forAggregatedMerchants(today.plusDays(date_idx).toString()).build(),
                    new StoreCapacityBOBuilder().build());
        return capacityMap;
    }

    private Map<CapacityDataItemUniqueKeys, StoreCapacityBO> getDefaultCapacityMapForIndividualMerchants() {
        Map<CapacityDataItemUniqueKeys, StoreCapacityBO> capacityMap = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityMap.put(new MerchantUniqueKeysBOBuilder()
                            .forIndividualMerchants(today.plusDays(date_idx).toString()).build(),
                    new StoreCapacityBOBuilder().build());
        return capacityMap;
    }

    private List<MerchantDetailsBO> getDefaultMerchantListForIndividualMerchants() {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        merchantList.add(new MerchantDetailsBOBuilder().forIndividualMerchants().build());
        return merchantList;
    }

    private List<OfferDetails> getDefaultOfferDetailsList() {
        List<OfferDetails> offerDetailsList = new ArrayList<>();
        offerDetailsList.add(OfferDetails.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                .isAggregated(false).build());
        offerDetailsList.add(OfferDetails.builder().merchantId(ConstantsClass.DUMMY_MERCHANT)
                .isAggregated(true).build());
        return offerDetailsList;
    }

    private List<String> getDefaultUnderlyingMerchantsIDList() {
        List<String> underlyingMerchantsList = new ArrayList<>();
        underlyingMerchantsList.add(ConstantsClass.INDIVIDUAL_MERCHANT_ID);
        return underlyingMerchantsList;
    }

    private List<CapacityDataBuilderInput> getDefaultCapacityDataBuilderInputListForAggregatedMerchants() {
        List<CapacityDataBuilderInput> capacityDataBuilderInputList = new ArrayList<>();
        capacityDataBuilderInputList.add(new CapacityDataBuilderInputBuilder().forAggregatedMerchants().build());
        return capacityDataBuilderInputList;
    }

    private List<CapacityDataBuilderInput> getDefaultCapacityDataBuilderInputListForIndividualMerchants() {
        List<CapacityDataBuilderInput> capacityDataBuilderInputList = new ArrayList<>();
        capacityDataBuilderInputList.add(new CapacityDataBuilderInputBuilder().forIndividualMerchants().build());
        return capacityDataBuilderInputList;
    }
}