package com.amazon.vas.ServiceCapacityTracker.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "CapacityData")
public class CapacityDataItem {
    @DynamoDBHashKey(attributeName = "Id")
    private String id;
    @DynamoDBRangeKey(attributeName = "Date")
    private String date;
    @DynamoDBAttribute(attributeName = "Asin")
    private String asin;
    @DynamoDBAttribute(attributeName = "MerchantId")
    private String merchantId;
    @DynamoDBAttribute(attributeName = "PinCode")
    private String pinCode;
    @DynamoDBAttribute(attributeName = "StoreId")
    private String storeId;
    @DynamoDBAttribute(attributeName = "TotalCapacity")
    private Integer totalCapacity;
    @DynamoDBAttribute(attributeName = "AvailableCapacity")
    private Integer availableCapacity;

    public String getId() {
        this.id = asin + merchantId + pinCode;
        return id;
    }
}
