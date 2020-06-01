package com.amazon.vas.ServiceCapacityTracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.amazon.vas.ServiceCapacityTracker.Activity.ServiceCapacityTrackerActivity;
import org.junit.Test;

public class ServiceCapacityTrackerActivityTest {
  @Test
  public void successfulResponse() {
    ServiceCapacityTrackerActivity serviceCapacityTrackerActivity = new ServiceCapacityTrackerActivity();
    /*GatewayResponse result = (GatewayResponse) app.handleRequest(null, null);
    assertEquals(result.getStatusCode(), 200);
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("\"message\""));
    assertTrue(content.contains("\"hello world\""));
    assertTrue(content.contains("\"location\""));*/
  }
}
