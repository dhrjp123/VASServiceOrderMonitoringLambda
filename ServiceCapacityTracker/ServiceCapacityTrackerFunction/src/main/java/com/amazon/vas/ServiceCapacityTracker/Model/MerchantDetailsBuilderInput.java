package com.amazon.vas.ServiceCapacityTracker.Model;

import com.google.inject.internal.util.$Nullable;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class MerchantDetailsBuilderInput
{
    @NonNull
    String asin;
    @NonNull
    String pinCode;
    @NonNull
    String marketplaceId;
    @NonNull
    List<String> underlyingMerchantsId;
}
