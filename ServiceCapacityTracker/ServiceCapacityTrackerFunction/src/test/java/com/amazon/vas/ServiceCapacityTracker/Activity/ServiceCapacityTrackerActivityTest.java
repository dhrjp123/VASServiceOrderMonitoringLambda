package com.amazon.vas.ServiceCapacityTracker.Activity;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.amazon.vas.ServiceCapacityTracker.Activity.ServiceCapacityTrackerActivity;
import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Exception.InvalidInputException;
import com.amazon.vas.ServiceCapacityTracker.Model.DailyCapacityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.EntityBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerRequestBO;
import com.amazon.vas.ServiceCapacityTracker.Model.ServiceCapacityTrackerResponseBO;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceCapacityTrackerActivityTest
{
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
    Map<String,String>input=getServiceCapacityTrackerInput("AC Installation", "");
    ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO=getDefaultServiceCapacityTrackerOutput();
    Object expectedServiceCapacityResponse=
            translateServiceCapacityTrackerResponseBOToJson(serviceCapacityTrackerResponseBO);
    when(serviceCapacityTrackerComponent.trackCapacity(any(ServiceCapacityTrackerRequestBO.class)))
            .thenReturn(serviceCapacityTrackerResponseBO);
    Object serviceCapacityTrackerResponse=serviceCapacityTrackerActivity.handleRequest(input);
    assertEquals(expectedServiceCapacityResponse.toString(),serviceCapacityTrackerResponse.toString());
  }
  @Test(expected = InvalidInputException.class)
  public void testHandleRequest_invalidInput()
  {
    Map<String,String>invalidInput=getServiceCapacityTrackerInput("","Delhi");
    serviceCapacityTrackerActivity.handleRequest(invalidInput);
  }
  public Map<String,String> getServiceCapacityTrackerInput(String serviceType, String cityName)
  {
    Map<String,String>input=new HashMap<>();
    input.put("serviceType",serviceType);
    input.put("cityName",cityName);
    return input;
  }
  public ServiceCapacityTrackerResponseBO getDefaultServiceCapacityTrackerOutput()
  {
    ArrayList<DailyCapacityBO> capacityList=new ArrayList<>();
    capacityList.add(new DailyCapacityBO(23,14));
    capacityList.add(new DailyCapacityBO(13,4));
    EntityBO entityBO1=new EntityBO("Delhi","",capacityList);
    EntityBO entityBO2=new EntityBO("Jaipur","",capacityList);
    ArrayList<EntityBO>entityList=new ArrayList<>();
    entityList.add(entityBO1);
    entityList.add(entityBO2);
    ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO=
            new ServiceCapacityTrackerResponseBO(entityList);
    return serviceCapacityTrackerResponseBO;
  }
  public String translateServiceCapacityTrackerResponseBOToJson(ServiceCapacityTrackerResponseBO serviceCapacityTrackerResponseBO)
  {
    Gson gson=new Gson();
    return gson.toJson(serviceCapacityTrackerResponseBO,ServiceCapacityTrackerResponseBO.class);
  }
}
