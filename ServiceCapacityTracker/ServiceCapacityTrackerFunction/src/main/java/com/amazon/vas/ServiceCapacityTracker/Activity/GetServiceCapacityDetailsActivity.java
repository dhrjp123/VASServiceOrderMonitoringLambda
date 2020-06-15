package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsInputBO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class GetServiceCapacityDetailsActivity {
    @NonNull
    private final ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;

    public GetServiceCapacityDetailsOutput handleRequest(
            @NonNull final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                translateToServiceCapacityDetailsInputBO(
                        getServiceCapacityDetailsInput);
        final ServiceCapacityDetailsBO serviceCapacityDetailsBO =
                serviceCapacityDetailsComponent.trackCapacity(serviceCapacityDetailsInputBO);
        return translateToGetServiceCapacityDetailsOutput(
                serviceCapacityDetailsBO);
    }

    public GetServiceCapacityDetailsOutput translateToGetServiceCapacityDetailsOutput
            (@NonNull final ServiceCapacityDetailsBO serviceCapacityDetailsBO) {
        return GetServiceCapacityDetailsOutput.builder()
                .storeList(serviceCapacityDetailsBO.getStoreList()).build();
    }

    public ServiceCapacityDetailsInputBO translateToServiceCapacityDetailsInputBO
            (@NonNull final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        return ServiceCapacityDetailsInputBO.builder()
                .skillType(getServiceCapacityDetailsInput.getSkillType())
                .storeName(getServiceCapacityDetailsInput.getStoreName())
                .marketplaceId(getServiceCapacityDetailsInput.getMarketplaceId()).build();
    }
}
