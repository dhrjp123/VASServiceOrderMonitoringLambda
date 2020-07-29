package model.spin;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class MerchantAggregatedDetails {
    @NonNull
    private String merchantName;
}
