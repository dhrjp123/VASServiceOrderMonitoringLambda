package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.ViewDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentResponseBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityDetailsBO;
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

public class ServiceCapacityTrackerComponentTest {
    final private static String SKILL_TYPE = "TV Installation";
    final private static String STORE_NAME = "";
    final private static String MARKETPLACE_ID = "India";
    private static final String STORE1_STORE_NAME = "Delhi";
    private static final String STORE1_MERCHANT_ID = "CMID1";
    private static final String STORE2_STORE_NAME = "Jaipur";
    private static final String STORE2_MERCHANT_ID = "CMID2";
    private static final int DAY1_TOTAL_CAPACITY = 23;
    private static final int DAY1_AVAILABLE_CAPACITY = 14;
    private static final int DAY2_TOTAL_CAPACITY = 13;
    private static final int DAY2_AVAILABLE_CAPACITY = 4;
    @InjectMocks
    private ServiceCapacityTrackerComponent serviceCapacityTrackerComponent;
    @Mock
    private ViewDataBuilder viewDataBuilder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTrackCapacity_withValidInput_thenSuccessfulResponse() {
        ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO =
                getDefaultServiceCapacityTrackerComponentRequestBO();
        ServiceCapacityTrackerComponentResponseBO expectedServiceCapacityTrackerComponentResponseBO
                = getDefaultServiceCapacityTrackerComponentResponseBO();
        when(viewDataBuilder.buildViewData(any(ServiceCapacityTrackerComponentRequestBO.class)))
                .thenReturn(expectedServiceCapacityTrackerComponentResponseBO);
        ServiceCapacityTrackerComponentResponseBO serviceCapacityTrackerComponentResponseBO
                = viewDataBuilder.buildViewData(serviceCapacityTrackerComponentRequestBO);
        assertEquals(expectedServiceCapacityTrackerComponentResponseBO.toString(),
                serviceCapacityTrackerComponentResponseBO.toString());
        verify(viewDataBuilder).buildViewData(any(ServiceCapacityTrackerComponentRequestBO.class));
    }

    @Test(expected = NullPointerException.class)
    public void testTrackCapacity_withNullInput() {
        serviceCapacityTrackerComponent.trackCapacity(null);
    }

    public ServiceCapacityTrackerComponentRequestBO getDefaultServiceCapacityTrackerComponentRequestBO() {
        return ServiceCapacityTrackerComponentRequestBO.builder()
                .storeName(STORE_NAME).marketplaceId(MARKETPLACE_ID).skillType(SKILL_TYPE).build();
    }

    public ServiceCapacityTrackerComponentResponseBO getDefaultServiceCapacityTrackerComponentResponseBO() {
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
        return ServiceCapacityTrackerComponentResponseBO.builder().storeList(storeList).build();
    }
}