package activity;

import component.GetJobMetricsComponent;
import lombok.NonNull;
import model.*;
import java.beans.IntrospectionException;
import java.io.IOException;

public class GetJobMetricsActivity {

    public GetJobMetricsOutput enact(@NonNull GetJobMetricsInput getJobMetricsInput) throws IOException, IntrospectionException {

            final GetJobMetricsComponent getJobMetricsComponent = new GetJobMetricsComponent();
            final GetJobMetricsInputBO getJobMetricsInputBO = new GetJobMetricsInputBO(getJobMetricsInput.getCity(),getJobMetricsInput.getMerchantID(),getJobMetricsInput.getServiceCategory(),getJobMetricsInput.getGroupingCriteria());
            final GetJobMetricsOutputBO getJobMetricsOutputBO = getJobMetricsComponent.getJobMetrics(getJobMetricsInputBO);
            final GetJobMetricsOutput getJobMetricsOutput = new GetJobMetricsOutput(getJobMetricsOutputBO.getMetaData(),getJobMetricsOutputBO.getJobMetricsMap());
            return getJobMetricsOutput;
    }
}
