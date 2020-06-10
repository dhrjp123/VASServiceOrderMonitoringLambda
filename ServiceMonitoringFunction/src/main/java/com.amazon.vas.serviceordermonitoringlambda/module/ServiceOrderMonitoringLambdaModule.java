package com.amazon.vas.serviceordermonitoringlambda.module;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.google.inject.Provides;
import lombok.NonNull;
import org.elasticsearch.client.RestHighLevelClient;

public class ServiceOrderMonitoringLambdaModule {
    @Provides
    GetJobMetricsActivity buildGetJobMetricsActivity(@NonNull GetJobMetricsComponent getJobMetricsComponent){
        return new GetJobMetricsActivity(getJobMetricsComponent);
    }

    @Provides
    GetJobMetricsComponent buildGetJobMetricsComponent(@NonNull JobDetailsBuilder jobDetailsBuilder){
        return new GetJobMetricsComponent(jobDetailsBuilder);
    }

    @Provides
    JobDetailsBuilder buildJobDetailsBuilder(@NonNull ElasticSearchAccessor elasticSearchAccessor){
        return new JobDetailsBuilder(elasticSearchAccessor);
    }

    @Provides
    ElasticSearchAccessor buildElasticSearchAccessor(@NonNull RestHighLevelClient elasticSearchClient){
        return new ElasticSearchAccessor(elasticSearchClient);
    }
}