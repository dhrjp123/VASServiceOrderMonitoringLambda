package com.amazon.vas.serviceordermonitoringlambda.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobAggregatedMetricsInputFilters {
    private String city;
    private String merchantId;
    private String technicianId;
}
