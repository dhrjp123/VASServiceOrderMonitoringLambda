package com.amazon.vas.serviceordermonitoringlambda.activity;

import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutput;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({ @Inject}))
public class GetJobMetricsActivity {

    @NonNull
    private final GetJobMetricsComponent getJobMetricsComponent;

    public GetJobMetricsOutput enact(@NonNull final GetJobMetricsInput getJobMetricsInput){

        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO(getJobMetricsInput);
        final GetJobMetricsOutputBO getJobMetricsOutputBO = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
        final GetJobMetricsOutput getJobMetricsOutput = buildGetJobMetricsOutput(getJobMetricsOutputBO);
        return getJobMetricsOutput;
    }
    private GetJobMetricsInputBO buildGetJobMetricsInputBO(GetJobMetricsInput getJobMetricsInput){
        return GetJobMetricsInputBO.builder().city(getJobMetricsInput.getCity()).merchantId(getJobMetricsInput.getMerchantId()).groupingCriteria(getJobMetricsInput.getGroupingCriteria()).build();
    }
    private GetJobMetricsOutput buildGetJobMetricsOutput(GetJobMetricsOutputBO getJobMetricsOutputBO){
        return GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData()).jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
    }
}
