package com.amazon.vas.serviceordermonitoringlambda.activity;

import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetJobMetricsActivity {

    @NonNull
    private final GetJobMetricsComponent getJobMetricsComponent;

    public GetJobMetricsOutput enact(@NonNull final GetJobMetricsInput getJobMetricsInput){

            final GetJobMetricsInputBO getJobMetricsInputBO = GetJobMetricsInputBO.builder().city(getJobMetricsInput.getCity()).merchantID(getJobMetricsInput.getMerchantID()).groupingCriteria(getJobMetricsInput.getGroupingCriteria()).build();
            final GetJobMetricsOutputBO getJobMetricsOutputBO = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
            final GetJobMetricsOutput getJobMetricsOutput = GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData()).jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
            return getJobMetricsOutput;
    }
}
