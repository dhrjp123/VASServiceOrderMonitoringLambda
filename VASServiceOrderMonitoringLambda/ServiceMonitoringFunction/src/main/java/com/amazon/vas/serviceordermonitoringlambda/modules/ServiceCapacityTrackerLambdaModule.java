package com.amazon.vas.serviceordermonitoringlambda.modules;

import com.amazon.vas.serviceordermonitoringlambda.accessor.DynamoDbAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.SPINServiceAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.VOSServiceAccessor;
import com.amazon.vas.serviceordermonitoringlambda.activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.serviceordermonitoringlambda.builder.MerchantDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.OfferDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.builder.StoreCapacityDetailsBOBuilder;
import com.amazon.vas.serviceordermonitoringlambda.component.ServiceCapacityDetailsComponent;
import com.amazon.vas.serviceordermonitoringlambda.config.AppConfig;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import lombok.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

public class ServiceCapacityTrackerLambdaModule extends AbstractModule {
    protected void configure() {
        bindConstant().annotatedWith(Names.named("region")).to(Regions.US_EAST_1);
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
        DefaultAWSCredentialsProviderChain credentialsProvider = new
                DefaultAWSCredentialsProviderChain();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(regions).build();
        return new DynamoDBMapper(client);
    }
}
