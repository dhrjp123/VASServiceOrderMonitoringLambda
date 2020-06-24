package com.amazon.vas.ServiceCapacityTracker.Model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ServiceCapacityDetailsBOBuilderInput implements Comparable {
    @NonNull
    private String asin;
    @NonNull
    private String pinCode;
    @NonNull
    private MerchantDetailsBO merchantDetailsBO;

    public int compareTo(Object object) {
        ServiceCapacityDetailsBOBuilderInput serviceCapacityDetailsBOBuilderInput =
                (ServiceCapacityDetailsBOBuilderInput) object;
        return merchantDetailsBO.getMerchantId()
                .compareTo(serviceCapacityDetailsBOBuilderInput.merchantDetailsBO.getMerchantId());
    }
}
