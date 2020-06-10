package com.amazon.vas.serviceordermonitoringlambda.model;

import lombok.Value;

@Value
public class GetJobDetailsInputBO {
    private final String city;
    private final String merchantID;
    private final String serviceCategory;
}
