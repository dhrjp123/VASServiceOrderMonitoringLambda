package activity;

import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;

import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;


import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.DataBuilder.CITY_NAME;
import static util.DataBuilder.MERCHANT_ID;
import static util.DataBuilder.EPOCH_TIME;


public class GetJobMetricsActivityTest {

    @Mock
    private GetJobMetricsComponent getJobMetricsComponent;

    @InjectMocks
    private GetJobMetricsActivity getJobMetricsActivity;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void getJobMetricsActivityTest(){

        when(getJobMetricsComponent.getJobMetrics(any(GetJobMetricsInputBO.class))).thenReturn(buildGetJobMetricsOutputBO());
        final GetJobMetricsOutput actualGetJobMetricsOutput = getJobMetricsActivity.enact(buildGetJobMetricsInput());
        final GetJobMetricsOutput expectedGetJobMetricsOutput = buildExpectedGetJobMetricsOutput();
        assertEquals(expectedGetJobMetricsOutput,actualGetJobMetricsOutput);
        verify(getJobMetricsComponent).getJobMetrics(any(GetJobMetricsInputBO.class));
    }

    private GetJobMetricsInput buildGetJobMetricsInput(){
        GetJobMetricsInput getJobMetricsInput = GetJobMetricsInput.builder().city(CITY_NAME).merchantID(MERCHANT_ID).serviceCategory(null).groupingCriteria(new ArrayList<>(Arrays.asList("city","time"))).build();
        return getJobMetricsInput;
    }
    private GetJobMetricsOutputBO buildGetJobMetricsOutputBO(){
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(CITY_NAME,EPOCH_TIME));

        final Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
            put(CITY_NAME,new HashMap<String, String>(){{
                put("name",CITY_NAME);
                put("ID",CITY_NAME);
            }});
        }};

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }
    private GetJobMetricsOutput buildExpectedGetJobMetricsOutput(){
        final JobAggregatedMetricsBO jobAggregatedMetricsBO = JobAggregatedMetricsBO.builder().totalJobsCount(1).statusNotUpdatedCount(1).otaFailureCount(1).etaDelayCount(1).build();
        final List<String> groupingCriteriaValues = new ArrayList<String>(Arrays.asList(CITY_NAME,EPOCH_TIME));
        Map<String, Map<String,String>> metaData = new HashMap<String,Map<String,String>>(){{
            put(CITY_NAME,new HashMap<String, String>(){{
                put("name",CITY_NAME);
                put("ID",CITY_NAME);
            }});
        }};

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = new HashMap<>();
        jobMetricsMap.put(groupingCriteriaValues,jobAggregatedMetricsBO);

        return GetJobMetricsOutput.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }
}
