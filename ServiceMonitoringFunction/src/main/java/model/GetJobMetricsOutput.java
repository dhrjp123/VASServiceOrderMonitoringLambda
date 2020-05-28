package model;

import lombok.Value;
import java.util.List;

@Value
public class GetJobMetricsOutput {
    private final List<JobAggregatedMetricsRow> jobAggregatedMetricsRowList;
};
