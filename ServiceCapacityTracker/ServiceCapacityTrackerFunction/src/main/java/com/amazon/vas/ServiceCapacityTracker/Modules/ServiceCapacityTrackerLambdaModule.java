package com.amazon.vas.ServiceCapacityTracker.Modules;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Activity.GetServiceCapacityDetailsActivity;
import com.amazon.vas.ServiceCapacityTracker.Builder.CapacityDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.MerchantDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Builder.OfferDetailsBuilder;
import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityDetailsComponent;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
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
            @NonNull ServiceCapacityDetailsComponent serviceCapacityDetailsComponent) {
        return new GetServiceCapacityDetailsActivity(serviceCapacityDetailsComponent);
    }

    @Singleton
    @Provides
    public ServiceCapacityDetailsComponent buildServiceCapacityTrackerComponent(
            @NonNull MerchantDetailsBuilder merchantDetailsBuilder, @NonNull OfferDetailsBuilder offerDetailsBuilder,
            @NonNull CapacityDataBuilder capacityDataBuilder, @NonNull AppConfig appConfig) {
        return new ServiceCapacityDetailsComponent(merchantDetailsBuilder, offerDetailsBuilder, capacityDataBuilder,
                appConfig);
    }

    @Singleton
    @Provides
    public OfferDetailsBuilder buildOfferDetailsBuilder(@NonNull VOSServiceAccessor vosServiceAccessor) {
        return new OfferDetailsBuilder(vosServiceAccessor);
    }

    @Singleton
    @Provides
    public MerchantDetailsBuilder buildMerchantDetailsBuilder(@NonNull SPINServiceAccessor spinServiceAccessor) {
        return new MerchantDetailsBuilder(spinServiceAccessor);
    }

    @Singleton
    @Provides
    public CapacityDataBuilder buildCapacityDataBuilder(@NonNull DynamoDbAccessor dynamoDbAccessor) {
        return new CapacityDataBuilder(dynamoDbAccessor);
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
    public DynamoDbAccessor buildDynamoDbAccessor(@NonNull DynamoDBMapper dynamoDBMapper) {
        return new DynamoDbAccessor(dynamoDBMapper);
    }

    @Singleton
    @Provides
    public DynamoDBMapper buildDynamoDBMapper(@Named("region") Regions regions) {
        DefaultAWSCredentialsProviderChain credentialsProvider = new
                DefaultAWSCredentialsProviderChain();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(regions).build();
        return new DynamoDBMapper(client);
    }
}
