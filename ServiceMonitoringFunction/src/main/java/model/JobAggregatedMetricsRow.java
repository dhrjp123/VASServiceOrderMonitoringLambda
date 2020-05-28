package model;

import lombok.Value;

import java.util.Map;

@Value
public class JobAggregatedMetricsRow {
    private final Map<String,String> entityMetaData;
    private final Map<String,JobAggregatedMetrics> jobAggregatedMetricsMap;
}
