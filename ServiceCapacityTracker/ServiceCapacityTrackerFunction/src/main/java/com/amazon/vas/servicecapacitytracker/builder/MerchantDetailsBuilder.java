package com.amazon.vas.servicecapacitytracker.builder;

import com.amazon.vas.servicecapacitytracker.accessor.SPINServiceAccessor;
import com.amazon.vas.servicecapacitytracker.model.MerchantDetailsBO;
import com.amazon.vas.servicecapacitytracker.model.spinservicemodel.GetMerchantAggregatedDetailsInput;
import com.amazon.vas.servicecapacitytracker.model.spinservicemodel.GetMerchantAggregatedDetailsOutput;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class MerchantDetailsBuilder {
    @NonNull
    private final SPINServiceAccessor spinServiceAccessor;

    public List<MerchantDetailsBO> getMerchants(@NonNull final String marketplaceId,
                                                @NonNull final List<String> merchantsId) {
        final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList = merchantsId.stream()
                .map(merchantId -> GetMerchantAggregatedDetailsInput.builder().encryptedMerchantId(merchantId)
                        .encryptedMarketplaceId(marketplaceId).build()).collect(Collectors.toList());
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                spinServiceAccessor.getGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsInputList);
        final List<MerchantDetailsBO> merchantList = new ArrayList<>();
        for (int merchant_idx = 0; merchant_idx < merchantsId.size(); merchant_idx++)
            merchantList.add(MerchantDetailsBO.builder().merchantId(merchantsId.get(merchant_idx))
                    .merchantName(
                            getMerchantAggregatedDetailsOutputList.get(merchant_idx).getMerchantAggregatedDetails()
                                    .getMerchantName()).build());
        return merchantList;
    }
}
