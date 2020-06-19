package com.amazon.vas.ServiceCapacityTracker.Activity;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetServiceCapacityDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsInputBO;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class GetServiceCapacityDetailsActivity {
    @NonNull
    private final ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;

    public GetServiceCapacityDetailsOutput handleRequest(
            @NonNull final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        validateInput(getServiceCapacityDetailsInput);
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                translateToServiceCapacityDetailsInputBO(
                        getServiceCapacityDetailsInput);
        final ServiceCapacityDetailsBO serviceCapacityDetailsBO =
                serviceCapacityDetailsComponent.trackCapacity(serviceCapacityDetailsInputBO);
        return translateToGetServiceCapacityDetailsOutput(
                serviceCapacityDetailsBO);
    }

    private GetServiceCapacityDetailsOutput translateToGetServiceCapacityDetailsOutput
            (final ServiceCapacityDetailsBO serviceCapacityDetailsBO) {
        return GetServiceCapacityDetailsOutput.builder()
                .storeList(serviceCapacityDetailsBO.getStoreList()).build();
    }

    private ServiceCapacityDetailsInputBO translateToServiceCapacityDetailsInputBO
            (final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        return ServiceCapacityDetailsInputBO.builder()
                .skillType(getServiceCapacityDetailsInput.getSkillType())
                .storeName(getServiceCapacityDetailsInput.getStoreName())
                .marketplaceId(getServiceCapacityDetailsInput.getMarketplaceId())
                .numberOfDays(getServiceCapacityDetailsInput.getNumberOfDays()).build();
    }

    private void validateInput(final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        if (StringUtils.isNullOrEmpty(getServiceCapacityDetailsInput.getSkillType()))
            throw new InvalidInputException("Skill Type can't be null");
    }
}
