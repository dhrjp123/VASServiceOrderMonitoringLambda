package model.activity;

import model.bo.JobAggregatedMetricsBO;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class GetJobMetricsOutput {
    private final Map<String, Map<String, String>> metaData;
    private final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap;
}
