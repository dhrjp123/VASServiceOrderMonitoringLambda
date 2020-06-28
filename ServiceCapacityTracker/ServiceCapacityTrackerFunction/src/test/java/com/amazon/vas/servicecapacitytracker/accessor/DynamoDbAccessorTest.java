package com.amazon.vas.servicecapacitytracker.accessor;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.exception.DependencyFailureException;
import com.amazon.vas.servicecapacitytracker.model.dynamodbmodel.CapacityDataItem;
import com.amazon.vas.servicecapacitytracker.testdata.builders.DefaultCapacityDataItemBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.google.common.collect.ImmutableList;
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

public class DynamoDbAccessorTest {
    @InjectMocks
    private DynamoDbAccessor dynamoDbAccessor;
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<CapacityDataItem> itemsToGet = ImmutableList.of(
                new DefaultCapacityDataItemBuilder(LocalDate.now().toString()).build());
        final Map<String, List<Object>> expectedCapacityItemMap = getDefaultCapacityItemMap();
        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenReturn(expectedCapacityItemMap);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        assertEquals(expectedCapacityItemMap, capacityItemMap);
        Mockito.verify(dynamoDBMapper).batchLoad(itemsToGet);
    }

    @Test(expected = DependencyFailureException.class)
    public void testGetItems_whenDynamoDBMapperThrowsException_thenThrowDependencyFailureException() {
        final DynamoDBMappingException exception = new DynamoDBMappingException();
        final List<CapacityDataItem> itemsToGet = ImmutableList.of(
                new DefaultCapacityDataItemBuilder(LocalDate.now().toString()).build());
        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenThrow(exception);
        dynamoDbAccessor.getItems(itemsToGet);
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        final Map<String, List<Object>> capacityItemMap = new HashMap<>();
        final List<Object> itemsToReceive = new ArrayList<>();
        itemsToReceive.add(new DefaultCapacityDataItemBuilder(LocalDate.now().toString()).withAllFields().build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}