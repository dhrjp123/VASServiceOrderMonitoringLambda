package com.amazon.vas.servicecapacitytracker.activity;

import com.amazon.vas.servicecapacitytracker.component.ServiceCapacityDetailsComponent;
import com.amazon.vas.servicecapacitytracker.exception.InvalidInputException;
import com.amazon.vas.servicecapacitytracker.model.*;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class GetServiceCapacityDetailsActivity {
    @NonNull
    private final ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;

    public GetServiceCapacityDetailsOutput handleRequest(
            @NonNull final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        validateInput(getServiceCapacityDetailsInput);
        final ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO =
                translateToServiceCapacityDetailsInputBO(getServiceCapacityDetailsInput);
        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList =
                serviceCapacityDetailsComponent.getStoreCapacityDetails(serviceCapacityDetailsInputBO);
        return createGetServiceCapacityDetailsOutput(storeCapacityDetailsBOList);
    }

    private GetServiceCapacityDetailsOutput createGetServiceCapacityDetailsOutput
            (final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList) {
        final List<StoreCapacityDetails> storeCapacityDetailsList = storeCapacityDetailsBOList.stream()
                .map(storeCapacityDetailsBO -> {
                    final List<StoreCapacity> storeCapacityList = storeCapacityDetailsBO.getCapacityList().stream()
                            .map(storeCapacityBO -> StoreCapacity.builder()
                                    .totalCapacity(storeCapacityBO.getTotalCapacity())
                                    .availableCapacity(storeCapacityBO.getAvailableCapacity()).build())
                            .collect(Collectors.toList());
                    return StoreCapacityDetails.builder().asin(storeCapacityDetailsBO.getAsin())
                            .pinCode(storeCapacityDetailsBO.getPinCode())
                            .storeName(storeCapacityDetailsBO.getStoreName())
                            .merchantId(storeCapacityDetailsBO.getMerchantId()).capacityList(storeCapacityList).build();
                }).sorted(Comparator.comparing(StoreCapacityDetails::getStoreName)).collect(Collectors.toList());
        return GetServiceCapacityDetailsOutput.builder().storeList(storeCapacityDetailsList).build();
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
