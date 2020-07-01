package com.amazon.vas.serviceordermonitoringlambda.builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.DynamoDbAccessor;
import com.amazon.vas.serviceordermonitoringlambda.constants.ConstantsClass;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBOBuilderInput;
import com.amazon.vas.serviceordermonitoringlambda.model.dynamodb.CapacityDataItem;
import com.amazon.vas.serviceordermonitoringlambda.testdata.builders.MockCapacityDataItemBuilder;
import com.amazon.vas.serviceordermonitoringlambda.testdata.builders.MockStoreCapacityDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.testdata.builders.MockStoreCapacityDetailsBOBuilderInputBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                Arrays.asList(new MockStoreCapacityDetailsBOBuilderInputBuilder().withAggregatedMerchants().build());
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new MockStoreCapacityDetailsBOBuilder().withAggregatedMerchants().build());

        Mockito.when(dynamoDbAccessor.getItems(getDefaultItemsToGetList().stream().map(
                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList())))
                .thenReturn(getDefaultCapacityItemMap());

        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = storeCapacityDetailsBOBuilder.getResponse(
                ConstantsClass.MARKETPLACE_ID, storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_DAYS);

        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
        Mockito.verify(dynamoDbAccessor).getItems(getDefaultItemsToGetList().stream().map(
                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList()));
    }

    private List<CapacityDataItem> getDefaultItemsToGetList() {
        final List<CapacityDataItem> itemsToGet = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_DAYS; date_idx++)
            itemsToGet.add(new MockCapacityDataItemBuilder(today.plusDays(date_idx).toString()).build());
        return ImmutableList.copyOf(itemsToGet);
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        final Map<String, List<Object>> capacityItemMap = new HashMap<>();
        List<Object> itemsToReceive = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_DAYS; date_idx++)
            itemsToReceive.add(new MockCapacityDataItemBuilder(today.plusDays(date_idx).toString()).withAllFields()
                    .build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}