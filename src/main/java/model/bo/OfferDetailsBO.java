package model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class OfferDetailsBO {
    @NonNull
    private String merchantId;
    @NonNull
    private boolean isAggregated;
}
