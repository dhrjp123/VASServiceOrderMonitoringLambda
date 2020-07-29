package activity;

import component.ServiceCapacityDetailsComponent;
import constants.ConstantsClass;
import exception.InvalidInputException;
import model.activity.GetServiceCapacityDetailsInput;
import model.activity.GetServiceCapacityDetailsOutput;
import model.activity.StoreCapacity;
import model.activity.StoreCapacityDetails;
import model.bo.StoreCapacityDetailsBO;
import testdata.builders.MockServiceCapacityDetailsInputBOBuilder;
import testdata.builders.MockStoreCapacityDetailsBOBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetServiceCapacityDetailsActivityTest {
    @Mock
    private ServiceCapacityDetailsComponent serviceCapacityDetailsComponent;
    @InjectMocks
    private GetServiceCapacityDetailsActivity getServiceCapacityDetailsActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRequest_whenValidInputIsPassed_thenSuccessfulResponse() {
        final GetServiceCapacityDetailsInput getServiceCapacityDetailsInput =
                getDefaultGetServiceCapacityDetailsInput();
        final GetServiceCapacityDetailsOutput expectedGetServiceCapacityDetailsOutput =
                getDefaultGetServiceCapacityDetailsOutput();
        final List<StoreCapacityDetailsBO> storeList = ImmutableList.of(new MockStoreCapacityDetailsBOBuilder()
                .withAggregatedMerchants().build());

        when(serviceCapacityDetailsComponent
                .getStoreCapacityDetails(
                        new MockServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build()))
                .thenReturn(storeList);

        final GetServiceCapacityDetailsOutput getServiceCapacityDetailsOutput = getServiceCapacityDetailsActivity.
                handleRequest(getServiceCapacityDetailsInput);

        Assert.assertEquals(expectedGetServiceCapacityDetailsOutput, getServiceCapacityDetailsOutput);
        verify(serviceCapacityDetailsComponent)
                .getStoreCapacityDetails(
                        new MockServiceCapacityDetailsInputBOBuilder().withEmptyStoreName().build());
    }

    @Test(expected = InvalidInputException.class)
    public void testHandleRequest_whenInputIsPassedWithEmptySkillType_thenThrowInvalidInputException() {
        getServiceCapacityDetailsActivity.handleRequest(GetServiceCapacityDetailsInput.builder().skillType("")
                .storeName(ConstantsClass.EMPTY_STORE_NAME).marketplaceId(ConstantsClass.MARKETPLACE_ID)
                .numberOfDays(ConstantsClass.NUMBER_OF_DAYS).build());
    }

    private GetServiceCapacityDetailsInput getDefaultGetServiceCapacityDetailsInput() {
        return GetServiceCapacityDetailsInput.builder()
                .skillType(ConstantsClass.SKILL_TYPE).storeName(ConstantsClass.EMPTY_STORE_NAME)
                .marketplaceId(ConstantsClass.MARKETPLACE_ID).numberOfDays(ConstantsClass.NUMBER_OF_DAYS).build();
    }

    private GetServiceCapacityDetailsOutput getDefaultGetServiceCapacityDetailsOutput() {
        final Map<LocalDate, StoreCapacity> capacityMap = new HashMap<>();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_DAYS; date_idx++)
            capacityMap.put(LocalDate.now().plusDays(date_idx), StoreCapacity.builder()
                    .totalCapacity(ConstantsClass.TOTAL_CAPACITY)
                    .availableCapacity(ConstantsClass.AVAILABLE_CAPACITY).build());
        final List<StoreCapacityDetails> storeList = ImmutableList.of(StoreCapacityDetails.builder()
                .storeName(ConstantsClass.AGGREGATED_MERCHANT_NAME).merchantId(ConstantsClass.AGGREGATED_MERCHANT_ID)
                .capacityMap(ImmutableMap.copyOf(capacityMap)).asin(ConstantsClass.ASIN)
                .pinCode(ConstantsClass.PINCODE).build());
        return GetServiceCapacityDetailsOutput.builder().storeList(storeList).build();
    }
}
