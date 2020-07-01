package com.amazon.vas.serviceordermonitoringlambda.model.bo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetJobMetricsInputBO {
    private final String city;
    private final String merchantId;
    private final String serviceCategory;
    private final List<String> groupingCriteria;
};
