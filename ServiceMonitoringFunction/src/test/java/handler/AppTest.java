package handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import accessor.ElasticSearchAccessor;
import activity.GetJobMetricsActivity;
import model.GetJobDetailsInput;
import model.GetJobMetricsInput;
import model.GetJobMetricsOutput;
import model.JobDetails;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class AppTest {
  @Test
  public void testActivity() throws IOException {
    List<String> groupingCriteria = new ArrayList<>();
    groupingCriteria.add("technicianID");
    groupingCriteria.add("time");
    GetJobMetricsInput input = new GetJobMetricsInput("City-5", "MerchantID-10", null, groupingCriteria);
    GetJobMetricsOutput output = new GetJobMetricsActivity().enact(input);
    System.out.println(output.toString());
  }

  @Test
  public void testAccessor() throws IOException{
    GetJobDetailsInput rawInput = new GetJobDetailsInput("City-5","MerchantId-4",null);
    ElasticSearchAccessor elasticSearchAccessor = new ElasticSearchAccessor();
    List<String> output = elasticSearchAccessor.getJobDetailsAccessor(rawInput);
    System.out.println(output.toString());
  }
}
