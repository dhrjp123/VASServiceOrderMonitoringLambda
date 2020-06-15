package com.amazon.vas.ServiceCapacityTracker.Accessor;

import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantAggregatedDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class SPINServiceAccessor {
    //Dummy Function to return some Dummy data
    private List<GetMerchantAggregatedDetailsOutput> getGetMerchantAggregatedDetailsOutputList
    (@NonNull final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList) {
        List<GetMerchantAggregatedDetailsOutput> list = new ArrayList<>();
        for (GetMerchantAggregatedDetailsInput getMerchantAggregatedDetailsInput :
                getMerchantAggregatedDetailsInputList) {
            String merchantId = getMerchantAggregatedDetailsInput.getEncryptedMerchantId();
            String merchantName = "Merchant" + merchantId.substring(3);
            MerchantAggregatedDetails merchantAggregatedDetails = MerchantAggregatedDetails.builder()
                    .merchantName(merchantName).build();
            list.add(GetMerchantAggregatedDetailsOutput.builder()
                    .merchantAggregatedDetails(merchantAggregatedDetails).build());
        }
        return list;
    }

    public List<GetMerchantAggregatedDetailsOutput> getMerchantNames(
            @NonNull final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList) {
        //We can call the SPIN service here. Currently, I am calling the dummy function.
        return getGetMerchantAggregatedDetailsOutputList(getMerchantAggregatedDetailsInputList);
    }
}
