package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.DynamoDbTableKeys;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class CapacityDataBuilder
{
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    final private static int NUMBER_OF_COLUMNS = 7;
    @NonNull
    private final DynamoDbAccessor dynamoDbAccessor;
    public Map<DynamoDbTableKeys, StoreCapacityBO> getCapacityMap(@NonNull final List<MerchantDetailsBO> merchantList)
    {
        final TableKeysAndAttributes tableKeysAndAttributes=buildTableKeysAndAttributes(merchantList);
        final List<Item>capacityDataItemList=dynamoDbAccessor.getItemList(tableKeysAndAttributes);
        Map<DynamoDbTableKeys, StoreCapacityBO> storeCapacityMap = new HashMap<>();
        for (Item item : capacityDataItemList) {
            storeCapacityMap.put(DynamoDbTableKeys.builder().Id(item.getString("Id"))
                            .date(item.getString("Date")).build(),
                    StoreCapacityBO.builder().totalCapacity(item.getInt("TotalCapacity"))
                            .availableCapacity(item.getInt("AvailableCapacity")).build());
        }
        return storeCapacityMap;
    }
    public TableKeysAndAttributes buildTableKeysAndAttributes(@NonNull final List<MerchantDetailsBO> merchantList)
    {
        TableKeysAndAttributes tableKeysAndAttributes=new TableKeysAndAttributes(DYNAMODB_TABLE_NAME);
        for(MerchantDetailsBO merchant : merchantList)
        {
            final String Id=merchant.getAsin()+merchant.getMerchantId()+merchant.getPinCode();
            final LocalDate today = LocalDate.now();
            for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                String date = today.plusDays(date_idx).toString();
                tableKeysAndAttributes.addHashAndRangePrimaryKey("Id", Id, "Date", date);
            }
        }
        return tableKeysAndAttributes;
    }
}
