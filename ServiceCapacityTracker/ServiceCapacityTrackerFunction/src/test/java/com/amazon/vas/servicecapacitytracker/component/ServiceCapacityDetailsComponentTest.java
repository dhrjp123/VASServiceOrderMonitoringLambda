package com.amazon.vas.servicecapacitytracker.component;

import com.amazon.vas.servicecapacitytracker.builder.MerchantDetailsBuilder;
import com.amazon.vas.servicecapacitytracker.builder.OfferDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.config.AppConfig;
import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.bo.CityDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.bo.OfferDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.bo.ServiceCapacityDetailsInputBO;
import com.amazon.vas.servicecapacitytracker.model.bo.StoreCapacityDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.bo.StoreCapacityDetailsBOBuilderInput;
import com.amazon.vas.servicecapacitytracker.testdata.builders.MockMerchantDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.MockServiceCapacityDetailsInputBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.MockStoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.MockStoreCapacityDetailsBOBuilderInputBuilder;
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
    private OfferDetailsBOBuilder offerDetailsBOBuilder;
    @Mock
    private StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @Mock
    private AppConfig appConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(appConfig.getCityMapper()).thenReturn(getDefaultCityMap());
        Mockito.when(appConfig.getAsinMapper()).thenReturn(getDefaultAsinMap());
    }

    @Test
    public void testGetStoreCapacityDetails_whenValidInputIsPassedWithEmptyStoreName_thenReturnAllAggregatedStoresCapacity() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new MockServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build();
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new MockStoreCapacityDetailsBOBuilder().withAggregatedMerchants().build());
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                ImmutableList
                        .of(new MockStoreCapacityDetailsBOBuilderInputBuilder().withAggregatedMerchants().build());

        Mockito.when(storeCapacityDetailsBOBuilder.getResponse(ConstantsClass.MARKETPLACE_ID,
                storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_DAYS))
                .thenReturn(expectedStoreCapacityDetailsBOList);

        List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = serviceCapacityDetailsComponent
                .getStoreCapacityDetails(serviceCapacityDetailsInputBO);

        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(storeCapacityDetailsBOBuilder)
                .getResponse(ConstantsClass.MARKETPLACE_ID, storeCapacityDetailsBOBuilderInputList,
                        ConstantsClass.NUMBER_OF_DAYS);
    }

    @Test
    public void testGetStoreCapacityDetails_whenValidInputIsPassedWithStoreName_thenReturnAllIndividualStoresCapacity() {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                new MockServiceCapacityDetailsInputBOBuilder().withStoreName().build();
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new MockStoreCapacityDetailsBOBuilder().withIndividualMerchants().build());
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                ImmutableList
                        .of(new MockStoreCapacityDetailsBOBuilderInputBuilder().withIndividualMerchants().build());

        Mockito.when(storeCapacityDetailsBOBuilder.getResponse(ConstantsClass.MARKETPLACE_ID,
                storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_DAYS))
                .thenReturn(expectedStoreCapacityDetailsBOList);
        Mockito.when(offerDetailsBOBuilder
                .getOfferDetailsBOList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE))
                .thenReturn(ImmutableList.of(OfferDetailsBO.builder().merchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .isAggregated(false).build(), OfferDetailsBO.builder().merchantId(ConstantsClass.DUMMY_MERCHANT)
                        .isAggregated(true).build()));
        Mockito.when(merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                ImmutableList.of(ConstantsClass.INDIVIDUAL_MERCHANT_ID)))
                .thenReturn(ImmutableList.of(new MockMerchantDetailsBOBuilder().withIndividualMerchants().build()));

        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = serviceCapacityDetailsComponent
                .getStoreCapacityDetails(serviceCapacityDetailsInputBO);

        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(storeCapacityDetailsBOBuilder)
                .getResponse(ConstantsClass.MARKETPLACE_ID, storeCapacityDetailsBOBuilderInputList,
                        ConstantsClass.NUMBER_OF_DAYS);
        Mockito.verify(offerDetailsBOBuilder)
                .getOfferDetailsBOList(ConstantsClass.MARKETPLACE_ID, ConstantsClass.ASIN, ConstantsClass.PINCODE);
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