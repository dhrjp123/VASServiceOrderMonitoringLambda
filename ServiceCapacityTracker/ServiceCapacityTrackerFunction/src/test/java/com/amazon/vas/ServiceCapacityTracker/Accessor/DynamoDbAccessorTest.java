package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Exception.DependencyFailureException;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.CapacityDataItemBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
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
        final List<Object> itemsToGet = getDefaultItemsToGetList();
        final Map<String, List<Object>> expectedCapacityItemMap = getDefaultCapacityItemMap();
        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenReturn(expectedCapacityItemMap);
        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet);
        assertEquals(expectedCapacityItemMap, capacityItemMap);
        Mockito.verify(dynamoDBMapper).batchLoad(itemsToGet);
    }

    @Test(expected = DependencyFailureException.class)
    public void testGetItems_whenDynamoDBMapperThrowsException_thenThrowDependencyFailureException() {
        final DynamoDBMappingException exception = new DynamoDBMappingException();
        final List<Object> itemsToGet = getDefaultItemsToGetList();
        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenThrow(exception);
        dynamoDbAccessor.getItems(itemsToGet);
    }

    private List<Object> getDefaultItemsToGetList() {
        List<Object> itemsToGet = new ArrayList<>();
        itemsToGet.add(new CapacityDataItemBuilder(LocalDate.now().toString()).build());
        return itemsToGet;
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        Map<String, List<Object>> capacityItemMap = new HashMap<>();
        List<Object> itemsToReceive = new ArrayList<>();
        itemsToReceive.add(new CapacityDataItemBuilder(LocalDate.now().toString()).withAllFields().build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}