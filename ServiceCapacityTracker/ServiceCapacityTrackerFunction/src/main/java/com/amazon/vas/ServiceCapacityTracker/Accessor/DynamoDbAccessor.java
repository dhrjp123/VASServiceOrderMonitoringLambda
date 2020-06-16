package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Exception.DependencyFailureException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class DynamoDbAccessor {
    @NonNull
    private DynamoDBMapper dynamoDBMapper;
    private static int BATCH_SIZE = 20;

    public Map<String, List<Object>> getItems(@NonNull final List<Object> itemsToGet) {
        try {
            List<List<Object>> itemsToGetBatchPartitions = Lists.partition(itemsToGet, BATCH_SIZE);
            Map<String, List<Object>> capacityItemMap = new HashMap<>();
            for (int batch_partition_idx = 0; batch_partition_idx < itemsToGetBatchPartitions.size();
                 batch_partition_idx++)
                capacityItemMap.putAll(dynamoDBMapper.batchLoad(itemsToGetBatchPartitions.get(batch_partition_idx)));
            return capacityItemMap;
        } catch (Exception e) {
            throw new DependencyFailureException(e);
        }
    }
}
