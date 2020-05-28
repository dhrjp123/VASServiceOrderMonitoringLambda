package activity;

import component.GetJobMetricsComponent;
import model.*;

import java.io.IOException;

public class GetJobMetricsActivity {

    public GetJobMetricsOutput enact(final GetJobMetricsInput input) throws IOException {

            GetJobMetricsComponent getJobMetricsComponent = new GetJobMetricsComponent();
            final GetJobMetricsOutput output = getJobMetricsComponent.getJobMetrics(input);
            return output;

    }
}
