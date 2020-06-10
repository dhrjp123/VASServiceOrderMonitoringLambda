package accessor;

import com.amazon.vas.serviceordermonitoringlambda.accessor.AWSRequestSigningApacheInterceptor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchClient;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ElasticSearchAccessorTest {
    @Mock
    private ElasticSearchClient elasticSearchClient;

    @InjectMocks
    private ElasticSearchAccessor elasticSearchAccessor;

    @Before
    public void setup() { MockitoAnnotations.initMocks(this); }

    @Test
    public void  elasticSearchAccessorTest(){
        when(ElasticSearchClient.getElasticSearchClient()).thenReturn(buildElasticSearchClient());

        final List<String> actualQueryVals = elasticSearchAccessor.esAccessor(buildSearchRequest());
        final List<String> expectedQueryVals = buildExpectedQueryVals();
        assertEquals(expectedQueryVals,actualQueryVals);
        verify(ElasticSearchClient.getElasticSearchClient());
    }

    private RestHighLevelClient buildElasticSearchClient() {
        final String serviceName = "es";
        final String region = "us-east-1";
        final String aesEndpoint = "https://search-test-ogmj2tg72y2re2mq3q2ffcjvyi.us-east-1.es.amazonaws.com";
        final String type = "_doc";

        final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();
        final AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        final HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, credentialsProvider);
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(aesEndpoint)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }
    private SearchRequest buildSearchRequest(){
        return new SearchRequest();
    }
    private List<String> buildExpectedQueryVals(){
        return new ArrayList<>();
    }

}
