package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.ServiceCapacityDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.DefaultServiceCapacityDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.MerchantDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.ServiceCapacityDetailsBOBuilderInputBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.ServiceCapacityDetailsInputBOBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    private ServiceCapacityDetailsBOBuilder serviceCapacityDetailsBOBuilder;
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
        final ServiceCapacityDetailsBO expectedServiceCapacityDetailsBO =
                new DefaultServiceCapacityDetailsBOBuilder().forAggregatedMerchants().build();
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(serviceCapacityDetailsBOBuilder.getResponse(
                getDefaultServiceCapacityDetailsBOBuilderInputListForAggregatedMerchants(),
                ConstantsClass.NUMBER_OF_COLUMNS)).thenReturn(expectedServiceCapacityDetailsBO);
        ServiceCapacityDetailsBO serviceCapacityDetailsBO = serviceCapacityDetailsComponent
                .trackCapacity(serviceCapacityDetailsInputBO);
        assertEquals(expectedServiceCapacityDetailsBO, serviceCapacityDetailsBO);
        Mockito.verify(serviceCapacityDetailsBOBuilder)
                .getResponse(getDefaultServiceCapacityDetailsBOBuilderInputListForAggregatedMerchants(),
                        ConstantsClass.NUMBER_OF_COLUMNS);
    }

    @Test
    public void testTrackCapacity_whenValidInputIsPassedWithStoreName_thenSuccessfulResponse() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new ServiceCapacityDetailsInputBOBuilder().withStoreName().build();
        final ServiceCapacityDetailsBO expectedServiceCapacityDetailsBO =
                new DefaultServiceCapacityDetailsBOBuilder().forIndividualMerchants().build();
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(serviceCapacityDetailsBOBuilder.getResponse(
                getDefaultServiceCapacityDetailsBOBuilderInputListForIndividualMerchants(),
                ConstantsClass.NUMBER_OF_COLUMNS)).thenReturn(expectedServiceCapacityDetailsBO);
        Mockito.when(offerDetailsBuilder
                .getOfferDetailsList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE
                )).thenReturn(getDefaultOfferDetailsList());
        Mockito.when(merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                getDefaultUnderlyingMerchantsIDList()))
                .thenReturn(getDefaultMerchantListForIndividualMerchants());
        final ServiceCapacityDetailsBO serviceCapacityDetailsBO = serviceCapacityDetailsComponent
                .trackCapacity(serviceCapacityDetailsInputBO);
        assertEquals(expectedServiceCapacityDetailsBO, serviceCapacityDetailsBO);
        Mockito.verify(serviceCapacityDetailsBOBuilder)
                .getResponse(getDefaultServiceCapacityDetailsBOBuilderInputListForIndividualMerchants(),
                        ConstantsClass.NUMBER_OF_COLUMNS);
        Mockito.verify(offerDetailsBuilder)
                .getOfferDetailsList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE
                );
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

    private List<ServiceCapacityDetailsBOBuilderInput> getDefaultServiceCapacityDetailsBOBuilderInputListForAggregatedMerchants() {
        List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList = new ArrayList<>();
        serviceCapacityDetailsBOBuilderInputList
                .add(new ServiceCapacityDetailsBOBuilderInputBuilder().forAggregatedMerchants().build());
        return serviceCapacityDetailsBOBuilderInputList;
    }

    private List<ServiceCapacityDetailsBOBuilderInput> getDefaultServiceCapacityDetailsBOBuilderInputListForIndividualMerchants() {
        List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList = new ArrayList<>();
        serviceCapacityDetailsBOBuilderInputList
                .add(new ServiceCapacityDetailsBOBuilderInputBuilder().forIndividualMerchants().build());
        return serviceCapacityDetailsBOBuilderInputList;
    }
}