package builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.DataBuilder.*;

public class JobDetailsBuilderTest {

    @Mock
    private ElasticSearchAccessor elasticSearchAccessor;

    @InjectMocks
    private JobDetailsBuilder jobDetailsBuilder;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void jobDetailsBuilderTest(){
        when(elasticSearchAccessor.esAccessor(any(SearchRequest.class))).thenReturn(buildJobDetailsJson());
        final ArrayList<JobDetailsBO> actualJobDetailsList = jobDetailsBuilder.getJobDetailsBuilder(buildGetJobMetricsInputBO());
        final ArrayList<JobDetailsBO> expectedJobDetailsList = buildExpectedJobDetailsList();
        assertEquals(expectedJobDetailsList,actualJobDetailsList);
        verify(elasticSearchAccessor).esAccessor(any(SearchRequest.class));
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO(){
        final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(CITY_NAME).merchantID(MERCHANT_ID).serviceCategory(null).groupingCriteria(new ArrayList<String>(Arrays.asList("city","time"))).build();
        return getJobMetricsInputBO;
    }
    private ArrayList<JobDetailsBO> buildExpectedJobDetailsList(){
        final Gson gson = new Gson();
        final List<String> jobDetailsJson = buildJobDetailsJson();
        final ArrayList<JobDetailsBO> expectedJobDetailsList = new ArrayList<>();
        for(String job:jobDetailsJson){
            expectedJobDetailsList.add(gson.fromJson(job,JobDetailsBO.class));
        }
        return expectedJobDetailsList;
    }
    private List<String> buildJobDetailsJson(){
        JSONObject json = new JSONObject();
        json.put("jdes", NOT_SERVICED);
        json.put("jbd",EPOCH_TIME);
        json.put("cty",CITY_NAME);
        json.put("tid",TECHNICIAN_ID);
        json.put("lse",true);
        json.put("isj" , true);
        json.put("mid",MERCHANT_ID);

        return new ArrayList<>(Arrays.asList(json.toJSONString()));
    }
}