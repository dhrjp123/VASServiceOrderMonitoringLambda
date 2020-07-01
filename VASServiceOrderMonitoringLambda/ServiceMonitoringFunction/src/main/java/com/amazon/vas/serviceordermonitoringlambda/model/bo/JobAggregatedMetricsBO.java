package com.amazon.vas.serviceordermonitoringlambda.model.bo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JobAggregatedMetricsBO {
    private final int totalJobsCount;
    private final int statusNotUpdatedCount;
    private final int otaFailureCount;
    private final int etaDelayCount;

};
