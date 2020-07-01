package com.amazon.vas.serviceordermonitoringlambda.accessor;

import com.amazon.vas.serviceordermonitoringlambda.constants.ConstantsClass;
import com.amazon.vas.serviceordermonitoringlambda.exception.DependencyFailureException;
import com.amazon.vas.serviceordermonitoringlambda.model.dynamodb.CapacityDataItem;
import com.amazon.vas.serviceordermonitoringlambda.testdata.builders.MockCapacityDataItemBuilder;
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
import java.util.stream.Collectors;

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
                new MockCapacityDataItemBuilder(LocalDate.now().toString()).build());
        final Map<String, List<Object>> expectedCapacityItemMap = getDefaultCapacityItemMap();

        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenReturn(expectedCapacityItemMap);

        final Map<String, List<Object>> capacityItemMap = dynamoDbAccessor.getItems(itemsToGet.stream().map(
                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList()));

        assertEquals(expectedCapacityItemMap, capacityItemMap);
        Mockito.verify(dynamoDBMapper).batchLoad(itemsToGet);
    }

    @Test(expected = DependencyFailureException.class)
    public void testGetItems_whenDynamoDBMapperThrowsException_thenThrowDependencyFailureException() {
        final DynamoDBMappingException exception = new DynamoDBMappingException();
        final List<CapacityDataItem> itemsToGet = ImmutableList.of(
                new MockCapacityDataItemBuilder(LocalDate.now().toString()).build());

        Mockito.when(dynamoDBMapper.batchLoad(itemsToGet)).thenThrow(exception);

        dynamoDbAccessor.getItems(itemsToGet.stream().map(capacityDataItem -> (Object) capacityDataItem).collect(
                Collectors.toList()));
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        final Map<String, List<Object>> capacityItemMap = new HashMap<>();
        final List<Object> itemsToReceive = new ArrayList<>();
        itemsToReceive.add(new MockCapacityDataItemBuilder(LocalDate.now().toString()).withAllFields().build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}