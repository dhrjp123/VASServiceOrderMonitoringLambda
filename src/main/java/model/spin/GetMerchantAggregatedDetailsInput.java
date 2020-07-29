package model.spin;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class GetMerchantAggregatedDetailsInput {
    @NonNull
    private String encryptedMarketplaceId;
    @NonNull
    private String encryptedMerchantId;
}
