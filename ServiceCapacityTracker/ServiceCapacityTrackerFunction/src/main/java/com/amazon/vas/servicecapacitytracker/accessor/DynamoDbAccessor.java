package com.amazon.vas.servicecapacitytracker.accessor;

import com.amazon.vas.servicecapacitytracker.exception.DependencyFailureException;
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
    private static final int BATCH_SIZE = 20;
    @NonNull
    private final DynamoDBMapper dynamoDBMapper;

    public Map<String, List<Object>> getItems(@NonNull final List<Object> items) {
        try {
            final List<List<Object>> itemsBatchPartitions = Lists.partition(items, BATCH_SIZE);
            final Map<String, List<Object>> capacityItemMap = new HashMap<>();
            for (int batch_partition_idx = 0; batch_partition_idx < itemsBatchPartitions.size();
                 batch_partition_idx++) {
                capacityItemMap.putAll(dynamoDBMapper.batchLoad(itemsBatchPartitions.get(batch_partition_idx)));
            }
            return capacityItemMap;
        } catch (final Exception e) {
            throw new DependencyFailureException(e);
        }
    }
}
