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
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import lombok.NonNull;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ServiceOrderMonitoringLambdaModule extends AbstractModule {
    static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("type")).to("_doc");
        bindConstant().annotatedWith(Names.named("region")).to("us-east-1");
        bindConstant().annotatedWith(Names.named("aesEndpoint"))
                .to("https://search-test-ogmj2tg72y2re2mq3q2ffcjvyi.us-east-1.es.amazonaws.com");
    }

    @Singleton
    @Provides
    public GetJobMetricsActivity buildGetJobMetricsActivity(@NonNull GetJobMetricsComponent getJobMetricsComponent) {
        return new GetJobMetricsActivity(getJobMetricsComponent);
    }

    @Provides
    GetJobMetricsComponent buildGetJobMetricsComponent(@NonNull JobDetailsBuilder jobDetailsBuilder) {
        return new GetJobMetricsComponent(jobDetailsBuilder);
    }

    @Provides
    JobDetailsBuilder buildJobDetailsBuilder(@NonNull ElasticSearchAccessor elasticSearchAccessor) {
        return new JobDetailsBuilder(elasticSearchAccessor);
    }

    @Provides
    ElasticSearchAccessor buildElasticSearchAccessor(RestHighLevelClient elasticSearchClient) {
        return new ElasticSearchAccessor(elasticSearchClient);
    }

    @Provides
    private static RestHighLevelClient buildElasticSearchClient(@Named("type") String type,
                                                                @Named("region") String region,
                                                                @Named("aesEndpoint") String aesEndpoint) {
        final String serviceName = "es";
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer,
                credentialsProvider);
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint))
                .setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }

}
