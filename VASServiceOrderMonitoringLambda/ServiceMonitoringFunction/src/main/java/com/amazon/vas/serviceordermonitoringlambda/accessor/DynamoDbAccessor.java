package com.amazon.vas.serviceordermonitoringlambda.accessor;

import com.amazon.vas.serviceordermonitoringlambda.exception.DependencyFailureException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
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
                Map<String, List<Object>> batchCapacityItemMap = dynamoDBMapper
                        .batchLoad(itemsBatchPartitions.get(batch_partition_idx));
                batchCapacityItemMap.forEach((k, v) -> capacityItemMap.merge(k, v, (v1, v2) -> {
                    List<Object> list = new ArrayList<>(v1);
                    list.addAll(v2);
                    return list;
                }));
            }
            return capacityItemMap;
        } catch (final Exception e) {
            throw new DependencyFailureException(e);
        }
    }
}
