package com.amazon.vas.ServiceCapacityTracker.TestData.Builders;

import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityDetailsInputBO;

public class ServiceCapacityDetailsInputBOBuilder {
    private ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO;

    public ServiceCapacityDetailsInputBO build() {
        return serviceCapacityDetailsInputBO;
    }

    public ServiceCapacityDetailsInputBOBuilder withEmptyStoreName() {
        serviceCapacityDetailsInputBO = ServiceCapacityDetailsInputBO.builder().skillType(ConstantsClass.SKILL_TYPE)
                .storeName(ConstantsClass.EMPTY_STORE_NAME).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build();
        return this;
    }

    public ServiceCapacityDetailsInputBOBuilder withStoreName() {
        serviceCapacityDetailsInputBO = ServiceCapacityDetailsInputBO.builder().skillType(ConstantsClass.SKILL_TYPE)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build();
        return this;
    }
}
