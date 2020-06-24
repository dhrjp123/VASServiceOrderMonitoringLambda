package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.DefaultServiceCapacityDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.ServiceCapacityDetailsInputBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.StoreCapacityBOBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput =
                getDefaultGetServiceCapacityDetailsInput();
        final GetServiceCapacityDetailsOutput expectedGetServiceCapacityDetailsOutput =
                getDefaultGetServiceCapacityDetailsOutput();
        when(serviceCapacityDetailsComponent
                .trackCapacity(new ServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build()))
                .thenReturn(new DefaultServiceCapacityDetailsBOBuilder().forAggregatedMerchants().build());
        final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput = getServiceCapacityDetailsActivity.
                handleRequest(getServiceCapacityDetailsInput);
        assertEquals(expectedGetServiceCapacityDetailsOutput, getServiceCapacityDetailsOutput);
        verify(serviceCapacityDetailsComponent).trackCapacity(new ServiceCapacityDetailsInputBOBuilder()
                .withEmptyStoreName().build());
    }

    @Test(expected = InvalidInputException.class)
    public void testHandleRequest_whenInputIsPassedWithEmptySkillType_thenThrowInvalidInputException() {
        getServiceCapacityDetailsActivity.handleRequest(getGetServiceCapacityDetailsInputWithEmptySkillType());
    }

    private GetServiceCapacityDetailsInput getGetServiceCapacityDetailsInputWithEmptySkillType() {
        return GetServiceCapacityDetailsInput.builder().skillType("").storeName(ConstantsClass.EMPTY_STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build();
    }

    private GetServiceCapacityDetailsInput getDefaultGetServiceCapacityDetailsInput() {
        return GetServiceCapacityDetailsInput.builder().skillType(ConstantsClass.SKILL_TYPE)
                .storeName(ConstantsClass.EMPTY_STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build();
    }

    private GetServiceCapacityDetailsOutput getDefaultGetServiceCapacityDetailsOutput() {
        List<StoreCapacityBO> capacityList = new ArrayList<>();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityList.add(new StoreCapacityBOBuilder().build());
        ImmutableList<StoreCapacityDetailsBO> storeList = ImmutableList.of(StoreCapacityDetailsBO.builder()
                .storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME).merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID)
                .capacityList(ImmutableList.copyOf(capacityList)).build());
        return GetServiceCapacityDetailsOutput.builder().storeList(storeList).build();
    }
}
