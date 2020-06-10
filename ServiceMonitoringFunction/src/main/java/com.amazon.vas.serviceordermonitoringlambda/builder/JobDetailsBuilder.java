package com.amazon.vas.serviceordermonitoringlambda.builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class JobDetailsBuilder {

    @NonNull
    private final ElasticSearchAccessor elasticSearchAccessor;

    private static final Gson gson = new Gson();

    public ArrayList<JobDetailsBO> getJobDetailsBuilder(@NonNull final GetJobMetricsInputBO getJobMetricsInputBO) {

        final QueryBuilder queryBuilder = buildJobDetailsESQuery(getJobMetricsInputBO);
        final SearchRequest searchRequest = buildJobDetailsESSearchRequest(queryBuilder);
        final List<String> jobDetailsJson = elasticSearchAccessor.esAccessor(searchRequest);

        final ArrayList<JobDetailsBO> jobDetailsList = new ArrayList<>();
        for(String job:jobDetailsJson){
            jobDetailsList.add(buildJobDetailsBO(job));
        }
        return jobDetailsList;
    }

    private QueryBuilder buildJobDetailsESQuery(final GetJobMetricsInputBO getJobMetricsInputBO){

        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotEmpty(getJobMetricsInputBO.getCity()) ) {
            queryBuilder = ((BoolQueryBuilder) queryBuilder).must(new MatchQueryBuilder("cty", getJobMetricsInputBO.getCity()));
        }
        if(StringUtils.isNotEmpty(getJobMetricsInputBO. getMerchantID()))  {
            queryBuilder = ((BoolQueryBuilder) queryBuilder).must(new MatchQueryBuilder("mid", getJobMetricsInputBO.getMerchantID()));
        }

        return queryBuilder;
    }

    private SearchRequest buildJobDetailsESSearchRequest(final QueryBuilder queryBuilder){

        final String index = "jobs";
        final SearchRequest searchRequest = new SearchRequest(index);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(5000);
        searchSourceBuilder.fetchSource(new String[]{"jid", "mid", "jbd", "lse", "isj", "cty", "jdes"},null);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(1L));

        return searchRequest;
    }

    private JobDetailsBO buildJobDetailsBO(final String jobDetailsJson){
        final JobDetailsBO jobDetailsBO = gson.fromJson(jobDetailsJson, JobDetailsBO.class);
        return jobDetailsBO;
    }

}