package com.amazon.vas.ServiceCapacityTracker.Modules;

import com.amazon.vas.ServiceCapacityTracker.Accessor.DynamoDbAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.SPINServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Accessor.VOSServiceAccessor;
import com.amazon.vas.ServiceCapacityTracker.Activity.ServiceCapacityTrackerActivity;
import com.amazon.vas.ServiceCapacityTracker.Builder.ViewDataBuilder;
import com.amazon.vas.ServiceCapacityTracker.Component.ServiceCapacityTrackerComponent;
import com.amazon.vas.ServiceCapacityTracker.Config.AppConfig;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.NonNull;

import javax.inject.Singleton;

public class ServiceCapacityTrackerLambdaModule extends AbstractModule {
    protected void configure() {
    }

    @Singleton
    @Provides
    public ServiceCapacityTrackerActivity buildServiceCapacityTrackerActivity(
            @NonNull ServiceCapacityTrackerComponent serviceCapacityTrackerComponent) {
        return new ServiceCapacityTrackerActivity(serviceCapacityTrackerComponent);
    }

    @Singleton
    @Provides
    public ServiceCapacityTrackerComponent buildServiceCapacityTrackerComponent(
            @NonNull ViewDataBuilder viewDataBuilder) {
        return new ServiceCapacityTrackerComponent(viewDataBuilder);
    }

    @Singleton
    @Provides
    public ViewDataBuilder buildViewDataBuilder(@NonNull SPINServiceAccessor spinServiceAccessor,
                                                @NonNull VOSServiceAccessor vosServiceAccessor,
                                                @NonNull DynamoDbAccessor dynamoDbAccessor,
                                                @NonNull AppConfig appConfig) {
        return new ViewDataBuilder(spinServiceAccessor, vosServiceAccessor, dynamoDbAccessor, appConfig);
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
    public DynamoDbAccessor buildDynamoDbAccessor(@NonNull DynamoDB dynamoDB) {
        return new DynamoDbAccessor(dynamoDB);
    }

    @Singleton
    @Provides
    public DynamoDB buildDynamoDB() {
        DefaultAWSCredentialsProviderChain credentialsProvider = new
                DefaultAWSCredentialsProviderChain();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1).build();
        return new DynamoDB(client);
    }
}
