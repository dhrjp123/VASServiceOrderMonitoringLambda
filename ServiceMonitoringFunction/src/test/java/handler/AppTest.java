package handler;

<<<<<<< HEAD
import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.handler.App;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
import com.google.gson.Gson;
import org.junit.Before;
=======
import activity.GetJobMetricsActivity;
import model.GetJobMetricsInput;
import model.GetJobMetricsOutput;
>>>>>>> 5699f3300f58f59a95fc959213e122aee91b541b
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.DataBuilder.CITY_NAME;
import static util.DataBuilder.EPOCH_TIME;

public class AppTest {

  @Mock
  private GetJobMetricsActivity getJobMetricsActivity;

  @InjectMocks
  private App app;

  @Before
  public void setup() { MockitoAnnotations.initMocks(this); }

  @Test
  public void testActivity() throws IOException, IntrospectionException {
<<<<<<< HEAD
    when(getJobMetricsActivity.enact(any(GetJobMetricsInput.class))).thenReturn(buildGetJobMetricsOutput());
    String actualJobMetricsOutputJson = app.handleRequest(buildLambdaInput(),new util.TestContext());
    String expectedJobMetricsOutputJson = buildExpectedGetJobMetricsOutputJson();
    assertEquals(expectedJobMetricsOutputJson,actualJobMetricsOutputJson);
    verify(getJobMetricsActivity).enact(any(GetJobMetricsInput.class));
  }

  private Map<Object,Object> buildLambdaInput(){
      Map<Object,Object> lambdaInput = new HashMap<Object, Object>() {{
        put("city",null);
        put("merchantID",null);
        put("serviceCategory",null);
        put("groupingCriteria",new ArrayList<String>(Arrays.asList("city","time")));
      }};

      return lambdaInput;
  }
  private GetJobMetricsOutput buildGetJobMetricsOutput(){
    JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).etaDelayCount(1).otaFailureCount(1).statusNotUpdatedCount(1).build();
    List<String> groupingCriteriaValues = new ArrayList<>();
    groupingCriteriaValues.add(CITY_NAME);
    groupingCriteriaValues.add(EPOCH_TIME);
    Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
      put(CITY_NAME,new HashMap<String, String>(){{
        put("name",CITY_NAME);
        put("ID",CITY_NAME);
      }});
    }};

    Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
    jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

    return GetJobMetricsOutput.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
  }

  private String buildExpectedGetJobMetricsOutputJson(){
    JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
    List<String> groupingCriteriaValues = new ArrayList<>();
    groupingCriteriaValues.add(CITY_NAME);
    groupingCriteriaValues.add(EPOCH_TIME);
    Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
      put(CITY_NAME,new HashMap<String, String>(){{
        put("name",CITY_NAME);
        put("ID",CITY_NAME);
      }});
    }};

    Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
    jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

    GetJobMetricsOutput getJobMetricsOutput = GetJobMetricsOutput.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    Gson gson = new Gson();
    return gson.toJson(getJobMetricsOutput);
  }

}
=======
    List<String> groupingCriteria = new ArrayList<>();
    groupingCriteria.add("city");
    groupingCriteria.add("time");
    GetJobMetricsInput getJobMetricsInput = new GetJobMetricsInput("City-5", "MerchantID-10", null, groupingCriteria);
    GetJobMetricsOutput getJobMetricsOutput = new GetJobMetricsActivity().enact(getJobMetricsInput);
    System.out.println(getJobMetricsOutput.toString());
  }

}
>>>>>>> 5699f3300f58f59a95fc959213e122aee91b541b
