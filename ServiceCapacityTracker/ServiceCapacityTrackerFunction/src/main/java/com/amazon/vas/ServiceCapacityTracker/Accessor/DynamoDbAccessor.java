package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Exception.DependencyFailureException;
import com.amazon.vas.ServiceCapacityTracker.Model.DynamoDbAccessorInput;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class DynamoDbAccessor {
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    final private static int NUMBER_OF_COLUMNS = 7;
    @Inject
    private DynamoDB dynamoDB;

    public List<Item> getCapacityData(@NonNull final List<DynamoDbAccessorInput> dynamoDbAccessorInputList) {
        try {
            TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(DYNAMODB_TABLE_NAME);
            for (DynamoDbAccessorInput dynamoDbAccessorInput : dynamoDbAccessorInputList) {
                String Id = dynamoDbAccessorInput.getAsin() + dynamoDbAccessorInput.getMerchantId() +
                        dynamoDbAccessorInput.getPinCode();
                LocalDate today = LocalDate.now();
                for (int date_idx = 0; date_idx < NUMBER_OF_COLUMNS; date_idx++) {
                    String date = today.plusDays(date_idx).toString();
                    tableKeysAndAttributes.addHashAndRangePrimaryKey("Id", Id, "Date", date);
                }
            }
            List<Item> capacityDataItemList = new ArrayList<>();
            Map<String, KeysAndAttributes> unprocessed = null;
            BatchGetItemOutcome outcome = dynamoDB.batchGetItem(tableKeysAndAttributes);
            do {
                capacityDataItemList.addAll(outcome.getTableItems().get(DYNAMODB_TABLE_NAME));
                unprocessed = outcome.getUnprocessedKeys();
                if (!unprocessed.isEmpty())
                    outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
            } while (!unprocessed.isEmpty());
            return capacityDataItemList;
        } catch (Exception e) {
            throw new DependencyFailureException(e);
        }
    }
}
