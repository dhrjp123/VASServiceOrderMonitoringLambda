package com.amazon.vas.servicecapacitytracker.model.activity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetServiceCapacityDetailsOutput {
    @NonNull
    private List<StoreCapacityDetails> storeList;
}
