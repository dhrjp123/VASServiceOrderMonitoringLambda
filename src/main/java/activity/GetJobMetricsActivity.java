package activity;

import component.GetJobMetricsComponent;
import model.activity.GetJobMetricsInput;
import model.activity.GetJobMetricsOutput;
import model.bo.GetJobMetricsInputBO;
import model.bo.GetJobMetricsOutputBO;
import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class GetJobMetricsActivity {

    @NonNull
    private final GetJobMetricsComponent getJobMetricsComponent;

    public GetJobMetricsOutput enact(@NonNull final GetJobMetricsInput getJobMetricsInput) {

        final GetJobMetricsInputBO getJobMetricsInputBO = buildGetJobMetricsInputBO(getJobMetricsInput);
        final GetJobMetricsOutputBO getJobMetricsOutputBO = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
        final GetJobMetricsOutput getJobMetricsOutput = buildGetJobMetricsOutput(getJobMetricsOutputBO);
        return getJobMetricsOutput;
    }

    private GetJobMetricsInputBO buildGetJobMetricsInputBO(GetJobMetricsInput getJobMetricsInput) {
        return GetJobMetricsInputBO.builder().city(getJobMetricsInput.getCity())
                .merchantId(getJobMetricsInput.getMerchantId())
                .groupingCriteria(getJobMetricsInput.getGroupingCriteria()).build();
    }

    private GetJobMetricsOutput buildGetJobMetricsOutput(GetJobMetricsOutputBO getJobMetricsOutputBO) {
        return GetJobMetricsOutput.builder().metaData(getJobMetricsOutputBO.getMetaData())
                .jobMetricsMap(getJobMetricsOutputBO.getJobMetricsMap()).build();
    }
}
