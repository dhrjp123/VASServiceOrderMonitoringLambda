package com.amazon.vas.serviceordermonitoringlambda.testdata.builders;

import com.amazon.vas.serviceordermonitoringlambda.constants.ConstantsClass;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.ServiceCapacityDetailsInputBO;

public class MockServiceCapacityDetailsInputBOBuilder {
    private ServiceCapacityDetailsInputBO serviceCapacityDetailsInputBO;

    public ServiceCapacityDetailsInputBO build() {
        return serviceCapacityDetailsInputBO;
    }

    public MockServiceCapacityDetailsInputBOBuilder withEmptyStoreName() {
        serviceCapacityDetailsInputBO = ServiceCapacityDetailsInputBO.builder().skillType(ConstantsClass.SKILL_TYPE)
                .storeName(ConstantsClass.EMPTY_STORE_NAME).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .numberOfDays(ConstantsClass.NUMBER_OF_DAYS).build();
        return this;
    }

    public MockServiceCapacityDetailsInputBOBuilder withStoreName() {
        serviceCapacityDetailsInputBO = ServiceCapacityDetailsInputBO.builder().skillType(ConstantsClass.SKILL_TYPE)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME)
                .numberOfDays(ConstantsClass.NUMBER_OF_DAYS).build();
        return this;
    }
}
