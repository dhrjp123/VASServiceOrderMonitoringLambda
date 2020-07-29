package model.vos;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder
@Value
public class OfferSelector {
    @NonNull
    private AddressInput addressInput;
}
