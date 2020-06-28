package com.amazon.vas.servicecapacitytracker.activity;

import com.amazon.vas.servicecapacitytracker.component.ServiceCapacityDetailsComponent;
import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.exception.InvalidInputException;
import com.amazon.vas.servicecapacitytracker.model.GetServiceCapacityDetailsInput;
import com.amazon.vas.servicecapacitytracker.model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBO;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultGetServiceCapacityDetailsInputBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultGetServiceCapacityDetailsOutputBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultServiceCapacityDetailsInputBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultStoreCapacityDetailsBOBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
                new DefaultGetServiceCapacityDetailsInputBuilder().build();
        final GetServiceCapacityDetailsOutput expectedGetServiceCapacityDetailsOutput =
                new DefaultGetServiceCapacityDetailsOutputBuilder().build();
        final List<StoreCapacityDetailsBO> storeList = ImmutableList.of(new DefaultStoreCapacityDetailsBOBuilder()
                .forAggregatedMerchants().build());
        when(serviceCapacityDetailsComponent
                .getStoreCapacityDetails(
                        new DefaultServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build()))
                .thenReturn(storeList);
        final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput = getServiceCapacityDetailsActivity.
                handleRequest(getServiceCapacityDetailsInput);
        Assert.assertEquals(expectedGetServiceCapacityDetailsOutput, getServiceCapacityDetailsOutput);
        verify(serviceCapacityDetailsComponent)
                .getStoreCapacityDetails(
                        new DefaultServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build());
    }

    @Test(expected = InvalidInputException.class)
    public void testHandleRequest_whenInputIsPassedWithEmptySkillType_thenThrowInvalidInputException() {
        getServiceCapacityDetailsActivity.handleRequest(GetServiceCapacityDetailsInput.builder().skillType("")
                .storeName(ConstantsClass.EMPTY_STORE_NAME).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build());
    }
}
