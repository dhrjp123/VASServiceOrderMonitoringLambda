package com.amazon.vas.ServiceCapacityTracker.Builder;

import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Constants.ConstantsClass;
import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsInput;
import com.amazon.vas.ServiceCapacityTracker.Model.GetMerchantAggregatedDetailsOutput;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantAggregatedDetails;
import com.amazon.vas.ServiceCapacityTracker.Model.MerchantDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.TestData.Builders.MerchantDetailsBOBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
        final List<String> merchantsId = getDefaultMerchantsId();
        final List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList =
                getDefaultGetMerchantAggregatedDetailsInputList();
        final List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList =
                getMerchantAggregatedDetailsOutputList();
        final List<MerchantDetailsBO> expectedMerchantsList = getDefaultMerchantsList();
        Mockito.when(spinServiceAccessor.getGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsInputList))
                .thenReturn(getMerchantAggregatedDetailsOutputList);
        final List<MerchantDetailsBO> merchantsList = merchantDetailsBuilder.getMerchants(ConstantsClass.MARKETPLACE_ID,
                merchantsId);
        assertEquals(expectedMerchantsList, merchantsList);
        Mockito.verify(spinServiceAccessor)
                .getGetMerchantAggregatedDetailsOutput(getMerchantAggregatedDetailsInputList);
    }

    private List<MerchantDetailsBO> getDefaultMerchantsList() {
        List<MerchantDetailsBO> merchantList = new ArrayList<>();
        merchantList.add(new MerchantDetailsBOBuilder().forIndividualMerchants().build());
        return merchantList;
    }

    private List<String> getDefaultMerchantsId() {
        List<String> merchantsId = new ArrayList<>();
        merchantsId.add(ConstantsClass.INDIVIDUAL_MERCHANT_ID);
        return merchantsId;
    }

    private List<GetMerchantAggregatedDetailsInput> getDefaultGetMerchantAggregatedDetailsInputList() {
        List<GetMerchantAggregatedDetailsInput> getMerchantAggregatedDetailsInputList = new ArrayList<>();
        getMerchantAggregatedDetailsInputList.add(GetMerchantAggregatedDetailsInput.builder()
                .encryptedMerchantId(ConstantsClass.INDIVIDUAL_MERCHANT_ID)
                .encryptedMarketplaceId(ConstantsClass.MARKETPLACE_ID).build());
        return getMerchantAggregatedDetailsInputList;
    }

    private List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList() {
        List<GetMerchantAggregatedDetailsOutput> getMerchantAggregatedDetailsOutputList = new ArrayList<>();
        MerchantAggregatedDetails merchantAggregatedDetails = MerchantAggregatedDetails.builder()
                .merchantName(ConstantsClass.INDIVIDUAL_MERCHANT_Name).build();
        getMerchantAggregatedDetailsOutputList.add(GetMerchantAggregatedDetailsOutput.builder()
                .merchantAggregatedDetails(merchantAggregatedDetails).build());
        return getMerchantAggregatedDetailsOutputList;
    }
}