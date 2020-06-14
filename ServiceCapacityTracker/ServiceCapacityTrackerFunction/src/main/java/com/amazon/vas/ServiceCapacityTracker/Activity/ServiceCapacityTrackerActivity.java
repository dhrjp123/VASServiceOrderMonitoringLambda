package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerActivityInput;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerActivityOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerComponentResponseBO;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.inject.Inject;

@AllArgsConstructor
public class ServiceCapacityTrackerActivity {
    @Inject
    private final ServiceCapacityTrackerComponent serviceCapacityTrackerComponent;

    public ServiceCapacityTrackerActivityOutput handleRequest(
            @NonNull final ServiceCapacityTrackerActivityInput serviceCapacityTrackerActivityInput) {
        ServiceCapacityTrackerComponentRequestBO serviceCapacityTrackerComponentRequestBO =
                translateServiceCapacityTrackerActivityInputToServiceCapacityTrackerComponentRequestBO(
                        serviceCapacityTrackerActivityInput);
        ServiceCapacityTrackerComponentResponseBO serviceCapacityTrackerComponentResponseBO =
                serviceCapacityTrackerComponent.trackCapacity(serviceCapacityTrackerComponentRequestBO);
        return translateServiceCapacityTrackerComponentResponseBOToServiceCapacityTrackerActivityOutput(
                serviceCapacityTrackerComponentResponseBO);
    }

    public ServiceCapacityTrackerActivityOutput translateServiceCapacityTrackerComponentResponseBOToServiceCapacityTrackerActivityOutput
            (@NonNull final ServiceCapacityTrackerComponentResponseBO serviceCapacityTrackerComponentResponseBO) {
        return ServiceCapacityTrackerActivityOutput.builder()
                .storeList(serviceCapacityTrackerComponentResponseBO.getStoreList()).build();
    }

    public ServiceCapacityTrackerComponentRequestBO translateServiceCapacityTrackerActivityInputToServiceCapacityTrackerComponentRequestBO
            (@NonNull final ServiceCapacityTrackerActivityInput serviceCapacityTrackerActivityInput) {
        return ServiceCapacityTrackerComponentRequestBO.builder()
                .skillType(serviceCapacityTrackerActivityInput.getSkillType())
                .storeName(serviceCapacityTrackerActivityInput.getStoreName())
                .marketplaceId(serviceCapacityTrackerActivityInput.getMarketplaceId()).build();
    }
}
