package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Value
public class StoreCapacityDetailsBO
{
    String storeName;
    String storeId;
    List<StoreCapacityBO> capacityList;
}
