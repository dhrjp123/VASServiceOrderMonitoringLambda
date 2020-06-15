package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
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
    private static final String STORE1_STORE_NAME = "Delhi";
    private static final String STORE1_MERCHANT_ID = "CMID1";
    private static final String STORE2_STORE_NAME = "Jaipur";
    private static final String STORE2_MERCHANT_ID = "CMID2";
    private static final int DAY1_TOTAL_CAPACITY = 23;
    private static final int DAY1_AVAILABLE_CAPACITY = 14;
    private static final int DAY2_TOTAL_CAPACITY = 13;
    private static final int DAY2_AVAILABLE_CAPACITY = 4;
    final private static String SKILL_TYPE = "TV Installation";
    final private static String STORE_NAME = "";
    final private static String MARKETPLACE_ID = "India";
    @Mock
    private ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;
    @InjectMocks
    private GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest_withValidInput_thenSuccessfulResponse() {
        GetServiceCapacityDetailsInput getServiceCapacityDetailsInput =
                getDefaultServiceCapacityTrackerActivityInput();
        GetServiceCapacityDetailsOutput expectedGetServiceCapacityDetailsOutput =
                getDefaultServiceCapacityTrackerActivityOutput();
        when(serviceCapacityDetailsComponent.trackCapacity(any(ServiceCapacityDetailsInputBO.class)))
                .thenReturn(translateServiceCapacityTrackerActivityOutputToServiceCapacityTrackerComponentResponseBO
                        (expectedGetServiceCapacityDetailsOutput));
        GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput = getServiceCapacityDetailsActivity.
                handleRequest(getServiceCapacityDetailsInput);
        assertEquals(expectedGetServiceCapacityDetailsOutput, getServiceCapacityDetailsOutput);
        verify(serviceCapacityDetailsComponent).trackCapacity(any(ServiceCapacityDetailsInputBO.class));
    }

    @Test(expected = NullPointerException.class)
    public void testHandleRequest_withNullInput() {
        getServiceCapacityDetailsActivity.handleRequest(null);
    }

    public GetServiceCapacityDetailsInput getDefaultServiceCapacityTrackerActivityInput() {
        return GetServiceCapacityDetailsInput.builder().skillType(SKILL_TYPE).storeName(STORE_NAME)
                .marketplaceId(MARKETPLACE_ID).build();
    }

    public GetServiceCapacityDetailsOutput getDefaultServiceCapacityTrackerActivityOutput() {
        ImmutableList<StoreCapacityBO> capacityList = ImmutableList.of(StoreCapacityBO.builder()
                        .totalCapacity(DAY1_TOTAL_CAPACITY).availableCapacity(DAY1_AVAILABLE_CAPACITY).build(),
                StoreCapacityBO.builder().totalCapacity(DAY2_TOTAL_CAPACITY)
                        .availableCapacity(DAY2_AVAILABLE_CAPACITY).build());
        StoreCapacityDetailsBO storeCapacityDetailsBO1 = StoreCapacityDetailsBO.builder().storeName(STORE1_STORE_NAME)
                .merchantId(STORE1_MERCHANT_ID).capacityList(capacityList).build();
        StoreCapacityDetailsBO storeCapacityDetailsBO2 = StoreCapacityDetailsBO.builder().storeName(STORE2_STORE_NAME)
                .merchantId(STORE2_MERCHANT_ID).capacityList(capacityList).build();
        List<StoreCapacityDetailsBO> storeList = new ArrayList<>();
        storeList.add(storeCapacityDetailsBO1);
        storeList.add(storeCapacityDetailsBO2);
        return GetServiceCapacityDetailsOutput.builder().storeList(storeList).build();
    }

    public ServiceCapacityDetailsBO translateServiceCapacityTrackerActivityOutputToServiceCapacityTrackerComponentResponseBO
            (GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput) {
        return ServiceCapacityDetailsBO.builder()
                .storeList(getServiceCapacityDetailsOutput.getStoreList()).build();
    }
}
