package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class ServiceCapacityTrackerResponseBO
{
    List<StoreCapacityDetailsBO> storeList;
}
