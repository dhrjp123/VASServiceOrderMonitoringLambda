package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBOBuilderInput;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.CapacityDataItemBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.DefaultServiceCapacityDetailsBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.ServiceCapacityDetailsBOBuilderInputBuilder;
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

public class ServiceCapacityDetailsBOBuilderTest {
    @InjectMocks
    private ServiceCapacityDetailsBOBuilder serviceCapacityDetailsBOBuilder;
    @Mock
    private DynamoDbAccessor dynamoDbAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCapacityMap_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList =
                getDefaultServiceCapacityDetailsBOBuilderInputList();
        final ServiceCapacityDetailsBO expectedServiceCapacityDetailsBO = new DefaultServiceCapacityDetailsBOBuilder()
                .forAggregatedMerchants().build();
        Mockito.when(dynamoDbAccessor.getItems(getDefaultItemsToGetList())).thenReturn(getDefaultCapacityItemMap());
        final ServiceCapacityDetailsBO serviceCapacityDetailsBO = serviceCapacityDetailsBOBuilder.getResponse(
                serviceCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS);
        assertEquals(expectedServiceCapacityDetailsBO, serviceCapacityDetailsBO);
        Mockito.verify(dynamoDbAccessor).getItems(getDefaultItemsToGetList());
    }

    private List<ServiceCapacityDetailsBOBuilderInput> getDefaultServiceCapacityDetailsBOBuilderInputList() {
        List<ServiceCapacityDetailsBOBuilderInput> serviceCapacityDetailsBOBuilderInputList = new ArrayList<>();
        serviceCapacityDetailsBOBuilderInputList
                .add(new ServiceCapacityDetailsBOBuilderInputBuilder().forAggregatedMerchants().build());
        return serviceCapacityDetailsBOBuilderInputList;
    }

    private List<Object> getDefaultItemsToGetList() {
        List<Object> itemsToGet = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            itemsToGet.add(new CapacityDataItemBuilder(today.plusDays(date_idx).toString()).build());
        return itemsToGet;
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        Map<String, List<Object>> capacityItemMap = new HashMap<>();
        List<Object> itemsToReceive = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            itemsToReceive
                    .add(new CapacityDataItemBuilder(today.plusDays(date_idx).toString()).withAllFields().build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}