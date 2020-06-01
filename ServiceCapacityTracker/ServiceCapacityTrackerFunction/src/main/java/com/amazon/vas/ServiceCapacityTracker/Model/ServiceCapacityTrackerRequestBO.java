package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceCapacityTrackerRequestBO
{
    private String serviceType;
    private String cityName;
}
