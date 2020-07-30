package builder;

import accessor.DynamoDbAccessor;
import accessor.SPINServiceAccessor;
import accessor.VOSServiceAccessor;
import component.ServiceCapacityDetailsComponent;
import config.AppConfig;
import constants.ConstantsClass;
import model.bo.CityDetailsBO;
import model.bo.ServiceCapacityDetailsInputBO;
import model.bo.StoreCapacityDetailsBO;
import model.bo.StoreCapacityDetailsBOBuilderInput;
import model.dynamodb.CapacityDataItem;
import model.vos.VasOffer;
import modules.ServiceOrderMonitoringLambdaModule;
import testdata.builders.MockCapacityDataItemBuilder;
import testdata.builders.MockStoreCapacityDetailsBOBuilder;
import testdata.builders.MockStoreCapacityDetailsBOBuilderInputBuilder;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class StoreCapacityDetailsBOBuilderTest {
    @InjectMocks
    private StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder;
    @Mock
    private DynamoDbAccessor dynamoDbAccessor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCapacityMap_whenValidInputIsPassed_thenSuccessfulResponse() {
        final List<StoreCapacityDetailsBOBuilderInput> storeCapacityDetailsBOBuilderInputList =
                Arrays.asList(new MockStoreCapacityDetailsBOBuilderInputBuilder().withAggregatedMerchants().build());
        final List<StoreCapacityDetailsBO> expectedStoreCapacityDetailsBOList =
                ImmutableList.of(new MockStoreCapacityDetailsBOBuilder().withAggregatedMerchants().build());

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("us-east-1").build();
        DynamoDBMapper dMapper = new DynamoDBMapper(client);
        DynamoDbAccessor dynamoDbAccessor = new DynamoDbAccessor(dMapper);

        ////Map<String, List<Object>> rtn = dynamoDbAccessor.getItems(getDefaultItemsToGetList());
        dynamoDbAccessor.writeItems(getDefaultItemsToGetList());
//
//        StoreCapacityDetailsBOBuilder storeCapacityDetailsBOBuilder =
//                new StoreCapacityDetailsBOBuilder(dynamoDbAccessor);
//
//        ServiceCapacityDetailsComponent serviceCapacityDetailsComponent =
//                new ServiceCapacityDetailsComponent(new MerchantDetailsBuilder(new SPINServiceAccessor()),
//                        new OfferDetailsBOBuilder(new VOSServiceAccessor()),
//                        storeCapacityDetailsBOBuilder,
//                        new AppConfig());
//
//        List<StoreCapacityDetailsBO> lst =
//                serviceCapacityDetailsComponent.getStoreCapacityDetails(ServiceCapacityDetailsInputBO.builder()
//                .marketplaceId("India")
//                .numberOfDays(1)
//                .skillType("AC Installation")
//                .storeName("Delhi").build()
//        );




//        Mockito.when(dynamoDbAccessor.getItems(getDefaultItemsToGetList().stream().map(
//                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList())))
//                .thenReturn(getDefaultCapacityItemMap());
//
//        final List<StoreCapacityDetailsBO> storeCapacityDetailsBOList = storeCapacityDetailsBOBuilder.getResponse(
//                ConstantsClass.MARKETPLACE_ID, storeCapacityDetailsBOBuilderInputList, ConstantsClass.NUMBER_OF_DAYS);
//
//        assertEquals(expectedStoreCapacityDetailsBOList, storeCapacityDetailsBOList);
//        Mockito.verify(dynamoDbAccessor).getItems(getDefaultItemsToGetList().stream().map(
//                capacityDataItem -> (Object) capacityDataItem).collect(Collectors.toList()));
    }




    private List<Object> getDefaultItemsToGetList() {

        List<CapacityDataItem> list = new ArrayList<>();
        Random random = new Random(0);
        List<String> asins = ImmutableList.of("AID1", "AID2" , "AID3");
        List<String> pins = ImmutableList.of("473001", "462003");

        for( int i = 0 ; i< 100 ; i++ ){
            String asin = asins.get(random.nextInt(3));
            String pin = pins.get(random.nextInt(2));
            String mid = "MID"+random.nextInt(10);

            for( int j = 0 ; j< 100 ; j++) {
                Date currDate = DateUtils.addDays(new Date(), j);
                String month = String.valueOf(currDate.getMonth());
                if(currDate.getMonth() < 9) month = "0" + month;

                String day = String.valueOf(currDate.getDay());
                if(currDate.getDay() < 9) day = "0" + day;

                final String date = "2020-"+month+"-"+day;
                ImmutableList.of("CMID1", "CMID2").forEach(p ->
                                list.add(getCapacityDataItem(asin, pin, p, date))
                );
                list.add(getCapacityDataItem(asin, pin, mid, "2020-"+month+"-"+day));
            }
        }
        Set<String> set = new HashSet<>(list.size());
        return list.stream().filter(p -> set.add(p.getId()+p.getDate())).collect(Collectors.toList());
    }


    private CapacityDataItem getCapacityDataItem(String asin, String pin, String merchantId, String date) {
        Random random = new Random();
        CapacityDataItem capacityDataItem = new CapacityDataItem();
        capacityDataItem.setAsin(asin);
        capacityDataItem.setDate(date);
        capacityDataItem.setMarketplaceId("India");
        capacityDataItem.setPinCode(pin);
        capacityDataItem.setMerchantId(merchantId);
        capacityDataItem.setAvailableCapacity(random.nextInt(100));
        capacityDataItem.setTotalCapacity(capacityDataItem.getAvailableCapacity() + random.nextInt(20));
        return capacityDataItem;
    }

    private Map<String, List<Object>> getDefaultCapacityItemMap() {
        final Map<String, List<Object>> capacityItemMap = new HashMap<>();
        List<Object> itemsToReceive = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int date_idx = 0; date_idx < ConstantsClass.NUMBER_OF_DAYS; date_idx++)
            itemsToReceive.add(new MockCapacityDataItemBuilder(today.plusDays(date_idx).toString()).withAllFields()
                    .build());
        capacityItemMap.put(ConstantsClass.DYNAMODB_TABLE_NAME, itemsToReceive);
        return capacityItemMap;
    }
}