package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class DynamoDbTableKeys
{
    @NonNull
    String Id;
    @NonNull
    String date;
}
