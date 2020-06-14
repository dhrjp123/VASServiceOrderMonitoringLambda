package com.amazon.vas.ServiceCapacityTracker.Component;

import com.amazon.vas.ServiceCapacityTracker.Builder.ViewDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentResponseBO;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.inject.Inject;

@AllArgsConstructor
public class ServiceCapacityTrackerComponent {
    @Inject
    final private ViewDataBuilder viewDataBuilder;

    public ServiceCapacityTrackerComponentResponseBO trackCapacity
            (@NonNull final ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO) {
        return viewDataBuilder.buildViewData(serviceCapacityTrackerComponentRequestBO);
    }
}
