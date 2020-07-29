package component;

import accessor.ElasticSearchAccessor;
import builder.JobDetailsBuilder;
import model.bo.GetJobMetricsInputBO;
import model.bo.GetJobMetricsOutputBO;
import model.bo.JobDetailsBO;
import testdata.builders.DefaultModelBuilders;
import util.JobAggregatedMetricsInputFilters;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;

import static constants.JobAggregatedMetricsConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetJobMetricsComponentTest {

    private final DefaultModelBuilders defaultModelBuilders = new DefaultModelBuilders();
    @Mock
    private JobDetailsBuilder jobDetailsBuilder;
    @InjectMocks
    private GetJobMetricsComponent getJobMetricsComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetJobMetricsComponent_withCityAndSlotStartTime_withNoFilter() {
        final GetJobMetricsInputBO getJobMetricsInputBO = defaultModelBuilders.buildGetJobMetricsInputBO(CITY);
        final JobAggregatedMetricsInputFilters filters = new JobAggregatedMetricsInputFilters();

        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO))
                .thenReturn((ArrayList<JobDetailsBO>) defaultModelBuilders.buildJobDetailsList(filters));
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput = defaultModelBuilders.buildGetJobMetricsOutputBO(CITY);
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);

        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);

        ElasticSearchAccessor elasticSearchAccessor = new ElasticSearchAccessor(null);
        JobDetailsBuilder jobDetailsBuilder = new JobDetailsBuilder(elasticSearchAccessor);
        GetJobMetricsComponent getJobMetricsComponent = new GetJobMetricsComponent(jobDetailsBuilder);
        GetJobMetricsOutputBO getJobMetricsOutputBO = getJobMetricsComponent.getJobMetrics(
                GetJobMetricsInputBO.builder()
                        .city("OOty")
                        .merchantId("MerchantId8")
                        .groupingCriteria(ImmutableList.of(TECHNICIAN_ID,SLOT_START_TIME)).build()
        );

    }

    @Test
    public void testGetJobMetricsComponent_withMerchantIdAndSlotStartTime_withCityFilter() {
        final GetJobMetricsInputBO getJobMetricsInputBO = defaultModelBuilders.buildGetJobMetricsInputBO(MERCHANT_ID);
        final JobAggregatedMetricsInputFilters filters = new JobAggregatedMetricsInputFilters();
        filters.setCity(TEST_CITY);
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO)).thenReturn(
                (ArrayList<JobDetailsBO>) defaultModelBuilders.buildJobDetailsList(filters));
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput =
                defaultModelBuilders.buildGetJobMetricsOutputBO(MERCHANT_ID);
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }

    @Test
    public void testGetJobMetricsComponent_withTechnicianIdAndSlotStartTime_withCityAndMerchantFilter() {
        final GetJobMetricsInputBO getJobMetricsInputBO = defaultModelBuilders.buildGetJobMetricsInputBO(TECHNICIAN_ID);
        final JobAggregatedMetricsInputFilters filters = new JobAggregatedMetricsInputFilters();
        filters.setCity(TEST_CITY);
        filters.setMerchantId(TEST_MERCHANT_ID);
        when(jobDetailsBuilder.getJobDetailsBuilder(getJobMetricsInputBO)).thenReturn(
                (ArrayList<JobDetailsBO>) defaultModelBuilders.buildJobDetailsList(filters));
        final GetJobMetricsOutputBO expectedGetJobMetricsOutput =
                defaultModelBuilders.buildGetJobMetricsOutputBO(TECHNICIAN_ID);
        final GetJobMetricsOutputBO actualGetJobMetricsOutput = getJobMetricsComponent
                .getJobMetrics(getJobMetricsInputBO);
        assertEquals(expectedGetJobMetricsOutput, actualGetJobMetricsOutput);
        verify(jobDetailsBuilder).getJobDetailsBuilder(getJobMetricsInputBO);
    }
}