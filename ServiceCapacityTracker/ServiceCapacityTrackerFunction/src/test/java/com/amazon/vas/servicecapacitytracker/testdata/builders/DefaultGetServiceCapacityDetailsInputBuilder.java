package com.amazon.vas.servicecapacitytracker.testdata.builders;

import com.amazon.vas.servicecapacitytracker.constants.ConstantsClass;
import com.amazon.vas.servicecapacitytracker.model.GetServiceCapacityDetailsInput;

public class DefaultGetServiceCapacityDetailsInputBuilder {
    private GetServiceCapacityDetailsInput getServiceCapacityDetailsInput;

    public DefaultGetServiceCapacityDetailsInputBuilder() {
        withDefaultValues();
    }

    private void withDefaultValues() {
        getServiceCapacityDetailsInput = GetServiceCapacityDetailsInput.builder()
                .skillType(ConstantsClass.SKILL_TYPE).storeName(ConstantsClass.EMPTY_STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).numberOfDays(ConstantsClass.NUMBER_OF_COLUMNS).build();
    }

    public GetServiceCapacityDetailsInput build() {
        return getServiceCapacityDetailsInput;
    }
}
