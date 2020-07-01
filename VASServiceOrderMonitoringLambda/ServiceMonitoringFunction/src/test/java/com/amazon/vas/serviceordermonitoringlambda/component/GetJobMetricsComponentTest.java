package com.amazon.vas.serviceordermonitoringlambda.component;

import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.bo.JobDetailsBO;
import com.amazon.vas.serviceordermonitoringlambda.testdata.builders.DefaultModelBuilders;
import com.amazon.vas.serviceordermonitoringlambda.util.JobAggregatedMetricsInputFilters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static com.amazon.vas.serviceordermonitoringlambda.constants.JobAggregatedMetricsConstants.*;
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