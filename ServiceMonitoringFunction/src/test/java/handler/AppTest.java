package handler;

import activity.GetJobMetricsActivity;
import model.GetJobMetricsInput;
import model.GetJobMetricsOutput;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppTest {
  @Test
  public void testActivity() throws IOException, IntrospectionException {
    List<String> groupingCriteria = new ArrayList<>();
    groupingCriteria.add("city");
    groupingCriteria.add("time");
    GetJobMetricsInput getJobMetricsInput = new GetJobMetricsInput("City-5", "MerchantID-10", null, groupingCriteria);
    GetJobMetricsOutput getJobMetricsOutput = new GetJobMetricsActivity().enact(getJobMetricsInput);
    System.out.println(getJobMetricsOutput.toString());
  }

}
