package model.bo;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetJobMetricsOutputBO {
    private final Map<String, Map<String, String>> metaData;
    private final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap;
}
