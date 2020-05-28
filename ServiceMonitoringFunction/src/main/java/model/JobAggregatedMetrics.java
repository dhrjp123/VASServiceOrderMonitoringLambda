package model;

import lombok.Value;

@Value
public class JobAggregatedMetrics {
    private final int totalJobs;
    private final int statusNotUpdated;
    private final int otaFailure;
    private final int etaDelay;

};


