package com.amazon.vas.serviceordermonitoringlambda.accessor;

import com.amazon.vas.serviceordermonitoringlambda.exception.DependencyFailureException;
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


@RequiredArgsConstructor
public class ElasticSearchAccessor {

        @NonNull
        private final RestHighLevelClient elasticSearchClient;

        private static Logger log = LogManager.getLogger(ElasticSearchAccessor.class);

        public List<String> esAccessor(@NonNull SearchRequest searchRequest){
                SearchResponse searchResponse = null;

                try {
                        searchResponse = elasticSearchClient.search(searchRequest, RequestOptions.DEFAULT);
                } catch(final Exception e){
                        throw new DependencyFailureException(e);
                }
                String scrollId = searchResponse.getScrollId();
                SearchHits hits = searchResponse.getHits();

                int docsCount = 0;
                final List<String> queryVals = new ArrayList<>();
                while(hits.getHits() != null && hits.getHits().length > 0){
                        docsCount += hits.getHits().length;
                        for(int i = 0; i < docsCount; i++) {
                                queryVals.add(hits.getHits()[i].getSourceAsString());
                        }

                        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                        scrollRequest.scroll(TimeValue.timeValueMinutes(1));
                        SearchResponse searchScrollResponse = null;
                        try {
                                searchScrollResponse = elasticSearchClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                        } catch(final Exception e){
                                throw new DependencyFailureException(e);
                        }
                        scrollId = searchScrollResponse.getScrollId();
                        hits = searchScrollResponse.getHits();
                }

                clearScroll(scrollId);
                return queryVals;
        }

        private void clearScroll(String scrollId){
                final ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                clearScrollRequest.addScrollId(scrollId);
                try {
                        elasticSearchClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
                } catch(final Exception e){
                        log.error(e.getMessage());
                }
        }
}