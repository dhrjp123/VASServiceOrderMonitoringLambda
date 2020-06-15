package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Exception.DependencyFailureException;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DynamoDbAccessor {
    final private static String DYNAMODB_TABLE_NAME = "CapacityData";
    @NonNull
    private DynamoDB dynamoDB;

    public List<Item> getItemList(@NonNull final TableKeysAndAttributes tableKeysAndAttributes) {
        try {
            List<Item> ItemList = new ArrayList<>();
            Map<String, KeysAndAttributes> unprocessed = null;
            BatchGetItemOutcome outcome = dynamoDB.batchGetItem(tableKeysAndAttributes);
            do {
                ItemList.addAll(outcome.getTableItems().get(DYNAMODB_TABLE_NAME));
                unprocessed = outcome.getUnprocessedKeys();
                if (!unprocessed.isEmpty())
                    outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
            } while (!unprocessed.isEmpty());
            return ItemList;
        } catch (Exception e) {
            throw new DependencyFailureException(e);
        }
    }
}
