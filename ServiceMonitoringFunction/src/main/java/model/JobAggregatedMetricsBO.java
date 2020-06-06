package model;

import lombok.Value;

@Value
public class JobAggregatedMetricsBO {
    private final int totalJobsCount;
    private final int statusNotUpdatedCount;
    private final int otaFailureCount;
    private final int etaDelayCount;

};


