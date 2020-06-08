package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Value
public class ServiceCapacityTrackerResponseBO
{
    private List<StoreCapacityDetailsBO> storeList;
}
