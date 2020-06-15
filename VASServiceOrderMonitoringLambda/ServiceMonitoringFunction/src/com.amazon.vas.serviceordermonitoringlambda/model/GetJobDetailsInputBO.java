package com.amazon.vas.serviceordermonitoringlambda.model;

import lombok.Value;

@Value
public class GetJobDetailsInputBO {
    private final String city;
    private final String merchantId;
    private final String serviceCategory;
}
