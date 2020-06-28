package com.amazon.vas.servicecapacitytracker.component;

import com.amazon.vas.servicecapacitytracker.builder.MerchantDetailsBuilder;
import com.amazon.vas.servicecapacitytracker.builder.OfferDetailsBuilder;
import com.amazon.vas.servicecapacitytracker.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.config.AppConfig;
import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.*;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultMerchantDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultServiceCapacityDetailsInputBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultStoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultStoreCapacityDetailsBOBuilderInputBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    private StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @Mock
    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetStoreCapacityDetails_whenValidInputIsPassedWithEmptyStoreName_thenSuccessfulResponse() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new DefaultServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build();
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new DefaultStoreCapacityDetailsBOBuilder().forAggregatedMerchants().build());
        List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                ImmutableList
                        .of(new DefaultStoreCapacityDetailsBOBuilderInputBuilder().forAggregatedMerchants().build());
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(storeCapacityDetailsBOBuilder.getResponse(
                storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS))
                .thenReturn(expectedStoreCapacityDetailsBOList);
        List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = serviceCapacityDetailsComponent
                .getStoreCapacityDetails(serviceCapacityDetailsInputBO);
        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(storeCapacityDetailsBOBuilder)
                .getResponse(storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS);
    }

    @Test
    public void testGetStoreCapacityDetails_whenValidInputIsPassedWithStoreName_thenSuccessfulResponse() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new DefaultServiceCapacityDetailsInputBOBuilder().withStoreName().build();
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new DefaultStoreCapacityDetailsBOBuilder().forIndividualMerchants().build());
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                ImmutableList
                        .of(new DefaultStoreCapacityDetailsBOBuilderInputBuilder().forIndividualMerchants().build());
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(storeCapacityDetailsBOBuilder.getResponse(
                storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS))
                .thenReturn(expectedStoreCapacityDetailsBOList);
        Mockito.when(offerDetailsBuilder
                .getOfferDetailsList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE))
                .thenReturn(ImmutableList.of(OfferDetails.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .isAggregated(false).build(), OfferDetails.builder().merchantId(ConstantsClass.DUMMY_MERCHANT)
                        .isAggregated(true).build()));
        Mockito.when(merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                ImmutableList.of(ConstantsClass.INDIVIDUAL_MERCHANT_ID)))
                .thenReturn(ImmutableList.of(new DefaultMerchantDetailsBOBuilder().forIndividualMerchants().build()));
        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = serviceCapacityDetailsComponent
                .getStoreCapacityDetails(serviceCapacityDetailsInputBO);
        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(storeCapacityDetailsBOBuilder)
                .getResponse(storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS);
        Mockito.verify(offerDetailsBuilder)
                .getOfferDetailsList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE);
        Mockito.verify(merchantDetailsBuilder).getMerchants(ConstantsClass.MARKETPLACE_ID,
                ImmutableList.of(ConstantsClass.INDIVIDUAL_MERCHANT_ID));
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
}