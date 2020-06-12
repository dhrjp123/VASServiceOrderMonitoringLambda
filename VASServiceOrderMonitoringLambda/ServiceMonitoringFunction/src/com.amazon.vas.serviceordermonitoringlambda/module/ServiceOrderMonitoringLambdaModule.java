package com.amazon.vas.serviceordermonitoringlambda.module;

import com.amazon.vas.serviceordermonitoringlambda.accessor.AWSRequestSigningApacheInterceptor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.NonNull;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import static com.amazon.vas.serviceordermonitoringlambda.util.ApplicationConstants.*;

public class ServiceOrderMonitoringLambdaModule extends AbstractModule {
    static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    @Singleton
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
    static RestHighLevelClient buildElasticSearchClient() {
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }

}