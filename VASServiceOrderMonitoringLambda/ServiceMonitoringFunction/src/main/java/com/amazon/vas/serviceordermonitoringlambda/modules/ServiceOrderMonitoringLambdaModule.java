package com.amazon.vas.serviceordermonitoringlambda.modules;

import com.amazon.vas.serviceordermonitoringlambda.accessor.AWSRequestSigningApacheInterceptor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.DynamoDbAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.SPINServiceAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.VOSServiceAccessor;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetJobMetricsActivity;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.MerchantDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.OfferDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.GetJobMetricsComponent;
import com.amazon.vas.serviceordermonitoringlambda.component.ServiceCapacityDetailsComponent;
import com.amazon.vas.serviceordermonitoringlambda.config.AppConfig;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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

    @Provides
    private static RestHighLevelClient buildElasticSearchClient(@Named("type") String type,
                                                         @Named("region") Regions region,
                                                         @Named("aesEndpoint") String aesEndpoint) {
        final String serviceName = "es";
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(String.valueOf(region));
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer,
                credentialsProvider);
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint))
                .setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }


    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("type")).to("_doc");
        bindConstant().annotatedWith(Names.named("region")).to(Regions.US_EAST_1);
        bindConstant().annotatedWith(Names.named("aesEndpoint"))
                .to("https://search-test-ogmj2tg72y2re2mq3q2ffcjvyi.us-east-1.es.amazonaws.com");
    }

    @Singleton
    @Provides
    public GetJobMetricsActivity buildGetJobMetricsActivity(@NonNull GetJobMetricsComponent getJobMetricsComponent) {
        return new GetJobMetricsActivity(getJobMetricsComponent);
    }

    @Provides
    public GetJobMetricsComponent buildGetJobMetricsComponent(@NonNull JobDetailsBuilder jobDetailsBuilder) {
        return new GetJobMetricsComponent(jobDetailsBuilder);
    }

    @Provides
    public JobDetailsBuilder buildJobDetailsBuilder(@NonNull ElasticSearchAccessor elasticSearchAccessor) {
        return new JobDetailsBuilder(elasticSearchAccessor);
    }

    @Provides
    public ElasticSearchAccessor buildElasticSearchAccessor(RestHighLevelClient elasticSearchClient) {
        return new ElasticSearchAccessor(elasticSearchClient);
    }

    @Singleton
    @Provides
    public GetServiceCapacityDetailsActivity buildServiceCapacityTrackerActivity(
            @NonNull final ServiceCapacityDetailsComponent serviceCapacityDetailsComponent) {
        return new GetServiceCapacityDetailsActivity(serviceCapacityDetailsComponent);
    }

    @Singleton
    @Provides
    public ServiceCapacityDetailsComponent buildServiceCapacityTrackerComponent(
            @NonNull final MerchantDetailsBuilder merchantDetailsBuilder,
            @NonNull final OfferDetailsBOBuilder offerDetailsBOBuilder,
            @NonNull final StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder,
            @NonNull final AppConfig appConfig) {
        return new ServiceCapacityDetailsComponent(merchantDetailsBuilder, offerDetailsBOBuilder,
                storeCapacityDetailsBOBuilder,
                appConfig);
    }

    @Singleton
    @Provides
    public OfferDetailsBOBuilder buildOfferDetailsBOBuilder(@NonNull final VOSServiceAccessor vosServiceAccessor) {
        return new OfferDetailsBOBuilder(vosServiceAccessor);
    }

    @Singleton
    @Provides
    public MerchantDetailsBuilder buildMerchantDetailsBuilder(@NonNull final SPINServiceAccessor spinServiceAccessor) {
        return new MerchantDetailsBuilder(spinServiceAccessor);
    }

    @Singleton
    @Provides
    public StoreCapacityDetailsBOBuilder buildStoreCapacityDetailsBOBuilder(
            @NonNull final DynamoDbAccessor dynamoDbAccessor) {
        return new StoreCapacityDetailsBOBuilder(dynamoDbAccessor);
    }

    @Singleton
    @Provides
    public VOSServiceAccessor buildVOSServiceAccessor() {
        return new VOSServiceAccessor();
    }

    @Singleton
    @Provides
    public SPINServiceAccessor buildSPINServiceAccessor() {
        return new SPINServiceAccessor();
    }

    @Singleton
    @Provides
    public DynamoDbAccessor buildDynamoDbAccessor(@NonNull final DynamoDBMapper dynamoDBMapper) {
        return new DynamoDbAccessor(dynamoDBMapper);
    }

    @Singleton
    @Provides
    public DynamoDBMapper buildDynamoDBMapper(@Named("region") final Regions regions) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(regions).build();
        return new DynamoDBMapper(client);
    }
}
