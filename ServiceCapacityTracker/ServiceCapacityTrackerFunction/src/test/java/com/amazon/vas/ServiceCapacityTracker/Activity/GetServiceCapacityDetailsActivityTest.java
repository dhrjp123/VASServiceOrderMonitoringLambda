package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetServiceCapacityDetailsActivityTest {
    @Mock
    private ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;
    @InjectMocks
    private GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest_whenValidInputIsPassed_thenSuccessfulResponse() {
        GetServiceCapacityDetailsInput getServiceCapacityDetailsInput =
                getDefaultGetServiceCapacityDetailsInput();
        GetServiceCapacityDetailsOutput expectedGetServiceCapacityDetailsOutput =
                getDefaultGetServiceCapacityDetailsOutput();
        when(serviceCapacityDetailsComponent
                .trackCapacity(translateToServiceCapacityDetailsInputBO(getServiceCapacityDetailsInput)))
                .thenReturn(translateToServiceCapacityDetailsBO
                        (expectedGetServiceCapacityDetailsOutput));
        GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput = getServiceCapacityDetailsActivity.
                handleRequest(getServiceCapacityDetailsInput);
        assertEquals(expectedGetServiceCapacityDetailsOutput, getServiceCapacityDetailsOutput);
        verify(serviceCapacityDetailsComponent).trackCapacity(any(ServiceCapacityDetailsInputBO.class));
    }

    @Test(expected = InvalidInputException.class)
    public void testHandleRequest_whenInputIsPassedWithEmptySkillType_thenThrowInvalidInputException() {
        getServiceCapacityDetailsActivity.handleRequest(getGetServiceCapacityDetailsInputWithEmptySkillType());
    }

    private GetServiceCapacityDetailsInput getGetServiceCapacityDetailsInputWithEmptySkillType() {
        return GetServiceCapacityDetailsInput.builder().skillType("").storeName(ConstantsClass.STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).build();
    }

    private GetServiceCapacityDetailsInput getDefaultGetServiceCapacityDetailsInput() {
        return GetServiceCapacityDetailsInput.builder().skillType(ConstantsClass.SKILL_TYPE)
                .storeName(ConstantsClass.STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).build();
    }

    private GetServiceCapacityDetailsOutput getDefaultGetServiceCapacityDetailsOutput() {
        ImmutableList<StoreCapacityBO> capacityList = ImmutableList.of(StoreCapacityBO.builder()
                        .totalCapacity(ConstantsClass.DAY1_TOTAL_CAPACITY)
                        .availableCapacity(ConstantsClass.DAY1_AVAILABLE_CAPACITY).build(),
                StoreCapacityBO.builder().totalCapacity(ConstantsClass.DAY2_TOTAL_CAPACITY)
                        .availableCapacity(ConstantsClass.DAY2_AVAILABLE_CAPACITY).build());
        StoreCapacityDetailsBO storeCapacityDetailsBO1 =
                StoreCapacityDetailsBO.builder().storeName(ConstantsClass.STORE1_STORE_NAME)
                        .merchantId(ConstantsClass.STORE1_MERCHANT_ID).capacityList(capacityList).build();
        StoreCapacityDetailsBO storeCapacityDetailsBO2 =
                StoreCapacityDetailsBO.builder().storeName(ConstantsClass.STORE2_STORE_NAME)
                        .merchantId(ConstantsClass.STORE2_MERCHANT_ID).capacityList(capacityList).build();
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        storeList.add(storeCapacityDetailsBO1);
        storeList.add(storeCapacityDetailsBO2);
        return GetServiceCapacityDetailsOutput.builder().storeList(storeList).build();
    }

    private ServiceCapacityDetailsBO translateToServiceCapacityDetailsBO
            (final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput) {
        return ServiceCapacityDetailsBO.builder()
                .storeList(getServiceCapacityDetailsOutput.getStoreList()).build();
    }

    private ServiceCapacityDetailsInputBO translateToServiceCapacityDetailsInputBO
            (final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        return ServiceCapacityDetailsInputBO.builder()
                .skillType(getServiceCapacityDetailsInput.getSkillType())
                .storeName(getServiceCapacityDetailsInput.getStoreName())
                .marketplaceId(getServiceCapacityDetailsInput.getMarketplaceId()).build();
    }
}
