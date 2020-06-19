package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataBuilderInput;
import com.amazon.vas.ServiceCapacityTracker.Model.CapacityDataItemUniqueKeys;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.CapacityDataBuilderInputBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.CapacityDataItemBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.MerchantUniqueKeysBOBuilder;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.StoreCapacityBOBuilder;
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

public class CapacityDataBuilderTest {
    @InjectMocks
    private CapacityDataBuilder capacityDataBuilder;
    @Mock
    private DynamoDbAccessor dynamoDbAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCapacityMap_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<CapacityDataBuilderInput> capacityDataBuilderInputList = getDefaultCapacityDataBuilderInputList();
        final Map<CapacityDataItemUniqueKeys, StoreCapacityBO> expectedCapacityMap = getDefaultCapacityMap();
        Mockito.when(dynamoDbAccessor.getItems(getDefaultItemsToGetList())).thenReturn(getDefaultCapacityItemMap());
        final Map<CapacityDataItemUniqueKeys, StoreCapacityBO> capacityMap = capacityDataBuilder.getCapacityMap(
                capacityDataBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS);
        assertEquals(expectedCapacityMap, capacityMap);
        Mockito.verify(dynamoDbAccessor).getItems(getDefaultItemsToGetList());
    }

    private Map<CapacityDataItemUniqueKeys, StoreCapacityBO> getDefaultCapacityMap() {
        Map<CapacityDataItemUniqueKeys, StoreCapacityBO> capacityMap = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            capacityMap.put(new MerchantUniqueKeysBOBuilder()
                            .forAggregatedMerchants(today.plusDays(date_idx).toString()).build(),
                    new StoreCapacityBOBuilder().build());
        return capacityMap;
    }

    private List<CapacityDataBuilderInput> getDefaultCapacityDataBuilderInputList() {
        List<CapacityDataBuilderInput> capacityDataBuilderInputList = new ArrayList<>();
        capacityDataBuilderInputList.add(new CapacityDataBuilderInputBuilder().forAggregatedMerchants().build());
        return capacityDataBuilderInputList;
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