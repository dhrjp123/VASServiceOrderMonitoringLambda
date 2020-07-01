package com.amazon.vas.serviceordermonitoringlambda.activity;

import com.amazon.vas.serviceordermonitoringlambda.component.ServiceCapacityDetailsComponent;
import com.amazon.vas.serviceordermonitoringlambda.exception.InvalidInputException;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetServiceCapacityDetailsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.GetServiceCapacityDetailsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.StoreCapacity;
import com.amazon.vas.serviceordermonitoringlambda.model.activity.StoreCapacityDetails;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.ServiceCapacityDetailsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.StoreCapacityDetailsBO;
import com.amazonaws.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    private GetServiceCapacityDetailsOutput createGetServiceCapacityDetailsOutput(
            final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList) {
        final List<StoreCapacityDetails> storeCapacityDetailsList = storeCapacityDetailsBOList.stream()
                .map(storeCapacityDetailsBO -> {
                    final Map<LocalDate, StoreCapacity> capacityMap = storeCapacityDetailsBO.getCapacityMap().entrySet()
                            .stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> translateToStoreCapacity(e.getValue())));
                    return StoreCapacityDetails.builder().asin(storeCapacityDetailsBO.getAsin())
                            .merchantId(storeCapacityDetailsBO.getMerchantId()).capacityMap(capacityMap)
                            .pinCode(storeCapacityDetailsBO.getPinCode())
                            .storeName(storeCapacityDetailsBO.getStoreName()).build();
                }).sorted(Comparator.comparing(StoreCapacityDetails::getStoreName)).collect(Collectors.toList());
        return GetServiceCapacityDetailsOutput.builder().storeList(storeCapacityDetailsList).build();
    }

    private StoreCapacity translateToStoreCapacity(final StoreCapacityBO storeCapacityBO) {
        return StoreCapacity.builder().availableCapacity(storeCapacityBO.getAvailableCapacity())
                .totalCapacity(storeCapacityBO.getTotalCapacity()).build();
    }

    private ServiceCapacityDetailsInputBO translateToServiceCapacityDetailsInputBO(
            final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        return ServiceCapacityDetailsInputBO.builder()
                .skillType(getServiceCapacityDetailsInput.getSkillType())
                .storeName(getServiceCapacityDetailsInput.getStoreName())
                .marketplaceId(getServiceCapacityDetailsInput.getMarketplaceId())
                .numberOfDays(getServiceCapacityDetailsInput.getNumberOfDays()).build();
    }

    private void validateInput(final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput) {
        if (StringUtils.isNullOrEmpty(getServiceCapacityDetailsInput.getSkillType())) {
            throw new InvalidInputException("Skill Type can't be null");
        }
    }
}
