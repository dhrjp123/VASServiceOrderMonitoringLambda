package com.amazon.vas.servicecapacitytracker.accessor;

import com.amazon.vas.servicecapacitytracker.model.spin.GetMerchantAggregatedDetailsInput;
import com.amazon.vas.servicecapacitytracker.model.spin.GetMerchantAggregatedDetailsOutput;
import com.amazon.vas.servicecapacitytracker.model.spin.MerchantAggregatedDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SPINServiceAccessor {
    public List<GetMerchantAggregatedDetailsOutput> getGetMerchantAggregatedDetailsOutput(
            @NonNull final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList) {
        // TODO: Call SPIN Service Client instead of returning Dummy Values
        return getGetMerchantAggregatedDetailsOutputList(getMerchantAggregatedDetailsInputList);
    }

    //Dummy Function to return some Dummy data
    private List<GetMerchantAggregatedDetailsOutput> getGetMerchantAggregatedDetailsOutputList(
            final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList) {
        final List<GetMerchantAggregatedDetailsOutput> list = getMerchantAggregatedDetailsInputList.stream()
                .map(getMerchantAggregatedDetailsInput -> {
                    final String merchantId = getMerchantAggregatedDetailsInput.getEncryptedMerchantId();
                    final String merchantName = "Merchant" + merchantId.substring(3);
                    return GetMerchantAggregatedDetailsOutput.builder().merchantAggregatedDetails(
                            MerchantAggregatedDetails.builder().merchantName(merchantName).build()).build();
                }).collect(Collectors.toList());
        return list;
    }
}
