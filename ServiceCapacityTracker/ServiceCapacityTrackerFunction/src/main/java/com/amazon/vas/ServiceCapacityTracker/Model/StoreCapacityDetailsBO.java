package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Value
public class StoreCapacityDetailsBO
{
    private String storeName;
    private String storeId;
    private List<StoreCapacityBO> capacityList;
}
