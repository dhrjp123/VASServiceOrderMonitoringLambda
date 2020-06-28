package com.amazon.vas.servicecapacitytracker.builder;

import com.amazon.vas.servicecapacitytracker.accessor.DynamoDbAccessor;
import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.StoreCapacityDetailsBOBuilderInput;
import com.amazon.vas.servicecapacitytracker.model.dynamodbmodel.CapacityDataItem;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultCapacityDataItemBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultStoreCapacityDetailsBOBuilder;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultStoreCapacityDetailsBOBuilderInputBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class StoreCapacityDetailsBOBuilderTest {
    @InjectMocks
    private StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @Mock
    private DynamoDbAccessor dynamoDbAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCapacityMap_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                Arrays.asList(new DefaultStoreCapacityDetailsBOBuilderInputBuilder().forAggregatedMerchants().build());
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new DefaultStoreCapacityDetailsBOBuilder().forAggregatedMerchants().build());
        Mockito.when(dynamoDbAccessor.getItems(getDefaultItemsToGetList())).thenReturn(getDefaultCapacityItemMap());
        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = storeCapacityDetailsBOBuilder.getResponse(
                storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_COLUMNS);
        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(dynamoDbAccessor).getItems(getDefaultItemsToGetList());
    }

    private List<CapacityDataItem> getDefaultItemsToGetList() {
        final List<CapacityDataItem> itemsToGet = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            itemsToGet.add(new DefaultCapacityDataItemBuilder(today.plusDays(date_idx).toString()).build());
        return ImmutableList.copyOf(itemsToGet);
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        final Map<String, List<Object>> capacityItemMap = new HashMap<>();
        List<Object> itemsToReceive = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_COLUMNS; date_idx++)
            itemsToReceive.add(new DefaultCapacityDataItemBuilder(today.plusDays(date_idx).toString()).withAllFields()
                    .build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}