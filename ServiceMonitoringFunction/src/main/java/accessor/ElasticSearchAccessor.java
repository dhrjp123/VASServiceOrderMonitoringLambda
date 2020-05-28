package accessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.GetJobDetailsInput;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

public class ElasticSearchAccessor {

        private static String serviceName = "es";
        private static String region = "us-east-1";
        private static String aesEndpoint = "https://search-test-ogmj2tg72y2re2mq3q2ffcjvyi.us-east-1.es.amazonaws.com";
        private static String index = "jobs";
        private static String type = "_doc";

        static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

        public List<String> getJobDetailsAccessor(GetJobDetailsInput getJobDetailsInput){
                return queryElasticSearch(getJobDetailsInput.getCity(),getJobDetailsInput.getSellerID(),getJobDetailsInput.getServiceCategory());
        }
        public List<String> queryElasticSearch(final String city, final String merchantID,final String serviceCategory) {

                RestHighLevelClient esClient = esClient(serviceName, region);
                SearchRequest searchRequest = new SearchRequest(index);
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

                QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                        .must(new MatchQueryBuilder("cty", city))
                        .must(new MatchQueryBuilder("mid", merchantID));

                searchSourceBuilder.query(queryBuilder);
                searchSourceBuilder.size(5000);
                searchSourceBuilder.fetchSource(new String[]{"jid", "mid", "jbd", "lse", "isj", "cty", "jdes"},null);
                searchRequest.source(searchSourceBuilder);
                searchRequest.scroll(TimeValue.timeValueMinutes(1L));
                SearchResponse searchResponse = null;
                try {
                        searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                String scrollId = searchResponse.getScrollId();
                SearchHits hits = searchResponse.getHits();

                int docsCount = 0;
                List<String> queryVals = new ArrayList<>();
                while(hits.getHits() != null && hits.getHits().length > 0) {

                        docsCount += hits.getHits().length;
                        for(int i = 0; i < docsCount; i++) {
                                queryVals.add(hits.getHits()[i].getSourceAsString());
                        }

                        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                        scrollRequest.scroll(TimeValue.timeValueMinutes(1));
                        SearchResponse searchScrollResponse = null;
                        try {
                                searchScrollResponse = esClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        scrollId = searchScrollResponse.getScrollId();
                        hits = searchScrollResponse.getHits();
                }

                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                try {
                        esClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
                } catch (IOException e) {
                        e.printStackTrace();
                }

                return queryVals;
        }

        public static RestHighLevelClient esClient(String serviceName, String region) {
                AWS4Signer signer = new AWS4Signer();
                signer.setServiceName(serviceName);
                signer.setRegionName(region);
                HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
                return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
        }

}

//
//
//package accessor;
//
//import com.amazonaws.util.StringUtils;
//import com.google.gson.Gson;
//import model.GetJobDetailsInput;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class ElasticSearchAccessor {
//
//        public List<String> getJobDetailsAccessor(GetJobDetailsInput getJobDetailsInput){
//                return queryElasticSearch(getJobDetailsInput.getCity(),getJobDetailsInput.getSellerID(),getJobDetailsInput.getServiceCategory());
//        }
//        public List<String> queryElasticSearch(final String city, final String merchantID,final String serviceCategory) {
//
//                Random random = new Random(0);
//                List<Map<String, Object>> docs = new ArrayList<>();
//                for( int i = 0 ; i< 10000; i++ ) {
//                        // Create the document as a hash map
//                        Map<String, Object> document = new HashMap<>();
//                        document.put("jid", "JobId-" + UUID.randomUUID().toString());
//
//                        int merchantUID = random.nextInt(20);
//                        document.put("mid", "MerchantID-"+ merchantUID);
//                        document.put("mname","Name-" + merchantUID);
//
//                        document.put("jbd", new Date(2020,05,27, 9 + random.nextInt(6), 0).getTime());
//
//                        int technicianUID = random.nextInt(200);
//                        document.put("tid", "Technician-" + technicianUID);
//                        document.put("tname", "Name-" + technicianUID);
//
//                        document.put("jdes", random.nextInt(10) == 9 ? "NotServiced":"JobStatus");
//                        document.put("asn", "ASIN-"+random.nextInt(30));
//                        document.put("lse", random.nextInt(2) == 0 ? true : false);
//                        document.put("isj", random.nextInt(2) == 0 ? true : false);
//                        document.put("cty", "City-" + random.nextInt(10));
//                        docs.add(document);
//                }
//
//                if(!StringUtils.isNullOrEmpty(city))
//                        docs = docs.stream().filter(p -> p.get("cty").equals(city)).collect(Collectors.toList());
//
//                if(!StringUtils.isNullOrEmpty(merchantID))
//                        docs = docs.stream().filter(p -> p.get("mid").equals(merchantID)).collect(Collectors.toList());
//
//                if(!StringUtils.isNullOrEmpty(serviceCategory))
//                        docs = docs.stream().filter(p -> p.get("asn").equals(serviceCategory)).collect(Collectors.toList());
//
//
//                List<String> outJsonList = docs.stream()
//                        .map(p -> new Gson().toJson(p))
//                        .collect(Collectors.toList());
//
//                return outJsonList;
//
//        }
//}
