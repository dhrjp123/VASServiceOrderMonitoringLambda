package builder;

import accessor.SPINServiceAccessor;
import constants.ConstantsClass;
import model.bo.MerchantDetailsBO;
import model.spin.GetMerchantAggregatedDetailsInput;
import model.spin.GetMerchantAggregatedDetailsOutput;
import model.spin.MerchantAggregatedDetails;
import testdata.builders.MockMerchantDetailsBOBuilder;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MerchantDetailsBuilderTest {
    @InjectMocks
    private MerchantDetailsBuilder merchantDetailsBuilder;
    @Mock
    private SPINServiceAccessor spinServiceAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMerchants_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<String> merchantsId = ImmutableList.of(ConstantsClass.INDIVIDUAL_MERCHANT_ID);
        final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList =
                ImmutableList.of(GetMerchantAggregatedDetailsInput.builder()
                        .encryptedMerchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                        .encryptedMarketplaceId(ConstantsClass.MARKETPLACE_ID).build());
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                ImmutableList.of(GetMerchantAggregatedDetailsOutput.builder()
                        .merchantAggregatedDetails(MerchantAggregatedDetails.builder()
                                .merchantName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).build()).build());
        final List<MerchantDetailsBO> expectedMerchantsList = ImmutableList.of(new MockMerchantDetailsBOBuilder()
                .withIndividualMerchants().build());

        Mockito.when(spinServiceAccessor.getGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsInputList))
                .thenReturn(getMerchantAggregatedDetailsOutputList);

        final List<MerchantDetailsBO> merchantsList = merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                merchantsId);

        assertEquals(expectedMerchantsList, merchantsList);
        Mockito.verify(spinServiceAccessor)
                .getGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsInputList);
    }
}