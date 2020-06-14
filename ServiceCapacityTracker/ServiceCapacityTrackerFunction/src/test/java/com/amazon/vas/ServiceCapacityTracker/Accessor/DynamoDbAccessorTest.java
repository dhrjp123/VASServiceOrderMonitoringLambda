package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DynamoDbAccessorTest {
    private static String ID = "AID1MID1473001";
    private static String DATE = LocalDate.now().toString();
    private static String ASIN = "AID1";
    private static String MERCHANTID = "MID1";
    private static String PINCODE = "473001";
    private static String STOREID = "SID1";
    private static String TOTALCAPACITY = "30";
    private static String AVAILABLECAPACITY = "21";
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    @InjectMocks
    private DynamoDbAccessor dynamoDbAccessor;
    @Mock
    private DynamoDB dynamoDB;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCapacityData_withValidInput_thenSuccessfulResponse() {
        TableKeysAndAttributes tableKeysAndAttributes = getDefaultTableKeysAndAttributes();
        BatchGetItemResult batchGetItemResult = getDefaultBatchGetItemResult();
        BatchGetItemOutcome expectedBatchGetItemOutcome = new BatchGetItemOutcome(batchGetItemResult);
        List<Item> expectedCapacityDataItemList = expectedBatchGetItemOutcome.getTableItems().get(DYNAMODB_TABLE_NAME);
        when(dynamoDB.batchGetItem(any(TableKeysAndAttributes.class)))
                .thenReturn(expectedBatchGetItemOutcome);
        BatchGetItemOutcome batchGetItemOutcome = dynamoDB.batchGetItem(tableKeysAndAttributes);
        List<Item> capacityDataItemList = batchGetItemOutcome.getTableItems().get(DYNAMODB_TABLE_NAME);
        assertEquals(expectedCapacityDataItemList.toString(), capacityDataItemList.toString());
        verify(dynamoDB).batchGetItem(any(TableKeysAndAttributes.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCapacityData_withNullInput() {
        dynamoDbAccessor.getCapacityData(null);
    }

    public TableKeysAndAttributes getDefaultTableKeysAndAttributes() {
        TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(DYNAMODB_TABLE_NAME);
        tableKeysAndAttributes.addHashAndRangePrimaryKey("Id", ID, "Date", DATE);
        return tableKeysAndAttributes;
    }

    public BatchGetItemResult getDefaultBatchGetItemResult() {
        List<Map<String, AttributeValue>> responseMapList = new ArrayList<>();
        Map<String, AttributeValue> responseMap = new HashMap<>();
        responseMap.put("Id", new AttributeValue().withS(ID));
        responseMap.put("Date", new AttributeValue().withS(DATE));
        responseMap.put("Asin", new AttributeValue().withS(ASIN));
        responseMap.put("MerchantId", new AttributeValue().withS(MERCHANTID));
        responseMap.put("PinCode", new AttributeValue().withS(PINCODE));
        responseMap.put("StoreId", new AttributeValue().withS(STOREID));
        responseMap.put("TotalCapacity", new AttributeValue().withN(TOTALCAPACITY));
        responseMap.put("AvailableCapacity", new AttributeValue().withN(AVAILABLECAPACITY));
        responseMapList.add(responseMap);
        return new BatchGetItemResult().addResponsesEntry("CapacityData", responseMapList);
    }
}