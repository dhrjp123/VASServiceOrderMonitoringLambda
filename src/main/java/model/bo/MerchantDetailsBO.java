package model.bo;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class MerchantDetailsBO {
    @NonNull
    private String merchantName;
    @NonNull
    private String merchantId;
}
