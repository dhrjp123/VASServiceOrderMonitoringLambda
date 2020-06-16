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

    public List<MerchantDetailsBO> getMerchants(@NonNull final String asin, @NonNull final String pinCode,
                                                @NonNull final String marketplaceId,
                                                @NonNull final List<String> merchantsId) {
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                spinServiceAccessor
                        .getGetMerchantAggregatedDetailsOutput(translateToGetMerchantAggregatedDetailsInputList(
                                merchantsId, marketplaceId));
        final List<String> merchantsName =
                getMerchantNamesFromGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsOutputList);
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        for (int merchant_idx = 0; merchant_idx < merchantsId.size(); merchant_idx++) {
            MerchantDetailsBO merchantDetailsBO = MerchantDetailsBO.builder().asin(asin).pinCode(pinCode)
                    .merchantId(merchantsId.get(merchant_idx))
                    .merchantName(merchantsName.get(merchant_idx)).build();
            merchantList.add(merchantDetailsBO);
        }
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

    private List<String> getMerchantNamesFromGetMerchantAggregatedDetailsOutput
            (final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList) {
        List<String> merchantsName = new ArrayList<>();
        for (GetMerchantAggregatedDetailsOutput getMerchantAggregatedDetailsOutput :
                getMerchantAggregatedDetailsOutputList)
            merchantsName.add(getMerchantAggregatedDetailsOutput.getMerchantAggregatedDetails().getMerchantName());
        return merchantsName;
    }
}
