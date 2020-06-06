package accessor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

import static accessor.provideESClient.esClient;


@RequiredArgsConstructor
public class ElasticSearchAccessor {

        private static final String serviceName = "es";
        private static final String region = "us-east-1";
        private static Logger log = LogManager.getLogger(ElasticSearchAccessor.class);
        private RestHighLevelClient esClient = esClient(serviceName, region);
        private void ClearScroll(String scrollId){

                ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                try {
                        esClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
                } catch(final Exception e){
                        log.error(e.getMessage());
                }
        }
        public List<String> esAccessor(@NonNull SearchRequest searchRequest){
                SearchResponse searchResponse = null;

                try {
                        searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
                } catch(final Exception e){
                        throw new DependencyFailureException(e);
                }
                String scrollId = searchResponse.getScrollId();
                SearchHits hits = searchResponse.getHits();

                int docsCount = 0;
                List<String> queryVals = new ArrayList<>();
                while(hits.getHits() != null && hits.getHits().length > 0){
                        docsCount += hits.getHits().length;
                        for(int i = 0; i < docsCount; i++) {
                                queryVals.add(hits.getHits()[i].getSourceAsString());
                        }

                        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                        scrollRequest.scroll(TimeValue.timeValueMinutes(1));
                        SearchResponse searchScrollResponse = null;
                        try {
                                searchScrollResponse = esClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                        } catch(final Exception e){
                                throw new DependencyFailureException(e);
                        }
                        scrollId = searchScrollResponse.getScrollId();
                        hits = searchScrollResponse.getHits();
                }

                ClearScroll(scrollId);
                return queryVals;
        }

}