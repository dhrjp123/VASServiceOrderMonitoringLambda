package com.amazon.vas.ServiceCapacityTracker.Activity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.StoreCapacityDetailsBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceCapacityTrackerActivityTest
{
  private static final String store1_storeName="Delhi";
  private static final String store1_storeId="12345";
  private static final String store2_storeName="Jaipur";
  private static final String store2_storeId="865re7";
  private static final int day1_totalCapacity=23;
  private static final int day1_availableCapacity=14;
  private static final int day2_totalCapacity=13;
  private static final int day2_availableCapacity=4;
  private static final String skillType="AC Installation";
  private static final String storeId="1qw23";
  private static final String marketplaceId="India";
  private static final String pinCode="1234";
  @Mock
  private ServiceCapacityTrackerComponent serviceCapacityTrackerComponent;
  @InjectMocks
  private ServiceCapacityTrackerActivity serviceCapacityTrackerActivity;
  @Before
  public void setup()
  {
    MockitoAnnotations.initMocks(this);
  }
  @Test
  public void testHandleRequest_successfulResponse()
  {
    Map<String,String>input=getServiceCapacityTrackerInput();
    ServiceCapacityTrackerRequestBO serviceCapacityTrackerRequestBO=
            translateInputToServiceCapacityTrackerRequestBO(input);
    ServiceCapacityTrackerResponseBO expectedServiceCapacityTrackerResponseBO=getDefaultServiceCapacityTrackerOutput();
    when(serviceCapacityTrackerComponent.trackCapacity(any(ServiceCapacityTrackerRequestBO.class)))
            .thenReturn(expectedServiceCapacityTrackerResponseBO);
    Object serviceCapacityTrackerResponseBO=serviceCapacityTrackerActivity.
            handleRequest(serviceCapacityTrackerRequestBO);
    assertEquals(expectedServiceCapacityTrackerResponseBO,serviceCapacityTrackerResponseBO);
  }
  public Map<String,String> getServiceCapacityTrackerInput()
  {
    Map<String,String>input=new HashMap<>();
    input.put("skillType",skillType);
    input.put("storeId",storeId);
    input.put("marketplaceId",marketplaceId);
    input.put("pinCode",pinCode);
    return input;
  }
  public ServiceCapacityTrackerResponseBO getDefaultServiceCapacityTrackerOutput()
  {
    ImmutableList<StoreCapacityBO> capacityList = ImmutableList.of(StoreCapacityBO.builder()
                    .totalCapacity(day1_totalCapacity).availableCapacity(day1_availableCapacity).build(),
            StoreCapacityBO.builder().totalCapacity(day2_totalCapacity)
                    .availableCapacity(day2_availableCapacity).build());
    StoreCapacityDetailsBO storeCapacityDetailsBO1 = StoreCapacityDetailsBO.builder().storeName(store1_storeName)
            .storeId(store1_storeId).capacityList(capacityList).build();
    StoreCapacityDetailsBO storeCapacityDetailsBO2 = StoreCapacityDetailsBO.builder().storeName(store2_storeName)
            .storeId(store2_storeId).capacityList(capacityList).build();
    ArrayList<StoreCapacityDetailsBO>storeList=new ArrayList<>();
    storeList.add(storeCapacityDetailsBO1);
    storeList.add(storeCapacityDetailsBO2);
    ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO=ServiceCapacityTrackerResponseBO.builder()
            .storeList(storeList).build();
    return serviceCapacityTrackerResponseBO;
  }
  public ServiceCapacityTrackerRequestBO translateInputToServiceCapacityTrackerRequestBO(@NonNull final Map<String,String> input)
  {
    return ServiceCapacityTrackerRequestBO.builder().skillType(input.get("skillType")).
            marketplaceId(input.get("marketplaceId")).pinCode(input.get("pinCode")).
            storeId(input.get("storeId")).build();
  }
}
