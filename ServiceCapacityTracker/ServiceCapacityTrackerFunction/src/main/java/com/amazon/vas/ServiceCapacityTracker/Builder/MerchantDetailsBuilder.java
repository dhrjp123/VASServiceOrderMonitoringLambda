package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Model.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class MerchantDetailsBuilder
{
    @NonNull
    private final SPINServiceAccessor spinServiceAccessor;
    public List<MerchantDetailsBO> getUnderlyingMerchants(@NonNull MerchantDetailsBuilderInput merchantDetailsBuilderInput)
    {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        final List<String> underlyingMerchantsName = getUnderlyingMerchantsName(merchantDetailsBuilderInput.getUnderlyingMerchantsId()
                , merchantDetailsBuilderInput.getMarketplaceId());
        for (int merchant_idx = 0; merchant_idx < merchantDetailsBuilderInput.getUnderlyingMerchantsId().size(); merchant_idx++) {
            MerchantDetailsBO merchantDetailsBO = MerchantDetailsBO.builder()
                    .merchantName(underlyingMerchantsName.get(merchant_idx))
                    .merchantId(merchantDetailsBuilderInput.getUnderlyingMerchantsId().get(merchant_idx))
                    .asin(merchantDetailsBuilderInput.getAsin()).pinCode(merchantDetailsBuilderInput.getPinCode()).build();
            merchantList.add(merchantDetailsBO);
        }
        return merchantList;
    }
    public List<String> getUnderlyingMerchantsName(@NonNull final List<String> underlyingMerchantsId,
                                                   String marketplaceId) {
        List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList = new ArrayList<>();
        for (String underlyingMerchantId : underlyingMerchantsId) {
            getMerchantAggregatedDetailsInputList.add(GetMerchantAggregatedDetailsInput.builder()
                    .encryptedMerchantId(underlyingMerchantId)
                    .encryptedMarketplaceId(marketplaceId).build());
        }
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                spinServiceAccessor.getMerchantNames(getMerchantAggregatedDetailsInputList);
        return getMerchantNamesFromGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsOutputList);
    }

    public List<String> getMerchantNamesFromGetMerchantAggregatedDetailsOutput
            (@NonNull final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList) {
        List<String> merchantNameList = new ArrayList<>();
        for (GetMerchantAggregatedDetailsOutput getMerchantAggregatedDetailsOutput :
                getMerchantAggregatedDetailsOutputList)
            merchantNameList.add(getMerchantAggregatedDetailsOutput.getMerchantAggregatedDetails().getMerchantName());
        return merchantNameList;
    }
}
