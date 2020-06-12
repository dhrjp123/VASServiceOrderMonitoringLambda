package com.amazon.vas.serviceordermonitoringlambda.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetJobMetricsInput {
    private final String city;
    private final String merchantId;
    private final String serviceCategory;
    private final List<String> groupingCriteria;
};
