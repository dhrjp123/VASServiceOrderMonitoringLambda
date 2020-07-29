package model.bo;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetJobMetricsInputBO {
    private final String city;
    private final String merchantId;
    private final String serviceCategory;
    private final List<String> groupingCriteria;
};
