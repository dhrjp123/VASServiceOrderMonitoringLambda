package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantDetailsBO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class MerchantDetailsBuilder {
    @NonNull
    private final SPINServiceAccessor spinServiceAccessor;

    public List<MerchantDetailsBO> getMerchants(@NonNull final String marketplaceId,
                                                @NonNull final List<String> merchantsId) {
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                spinServiceAccessor
                        .getGetMerchantAggregatedDetailsOutput(translateToGetMerchantAggregatedDetailsInputList(
                                merchantsId, marketplaceId));
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        for (int merchant_idx = 0; merchant_idx < merchantsId.size(); merchant_idx++)
            merchantList.add(MerchantDetailsBO.builder().merchantId(merchantsId.get(merchant_idx))
                    .merchantName(
                            getMerchantAggregatedDetailsOutputList.get(merchant_idx).getMerchantAggregatedDetails()
                                    .getMerchantName()).build());
        return merchantList;
    }

    private List<GetMerchantAggregatedDetailsInput> translateToGetMerchantAggregatedDetailsInputList(
            final List<String> merchantsId, final String marketplaceId) {
        List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList = new ArrayList<>();
        for (String merchantId : merchantsId) {
            getMerchantAggregatedDetailsInputList.add(GetMerchantAggregatedDetailsInput.builder()
                    .encryptedMerchantId(merchantId)
                    .encryptedMarketplaceId(marketplaceId).build());
        }
        return getMerchantAggregatedDetailsInputList;
    }
}
