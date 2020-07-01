package com.amazon.vas.serviceordermonitoringlambda.model.bo;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class GetJobMetricsOutputBO {
    private final Map<String, Map<String, String>> metaData;
    private final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap;
}
