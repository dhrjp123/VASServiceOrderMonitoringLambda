package model;

import lombok.Value;

import java.util.List;

@Value
public class GetJobMetricsInput {
    private final String city;
    private final String merchantID;
    private final String serviceCategory;
    private final List<String> groupingCriteria;
};
