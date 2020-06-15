package com.amazon.vas.serviceordermonitoringlambda.builder;

import com.amazon.vas.serviceordermonitoringlambda.accessor.ElasticSearchAccessor;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.gson.Gson;
import com.google.inject.Inject;
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


@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class JobDetailsBuilder {

    @NonNull
    private final ElasticSearchAccessor elasticSearchAccessor;

    private static final Gson gson = new Gson();
    private static final String JOB_ES_INDEX = "jobs";
    private static final long SEARCH_SCROLL_TIMEOUT = 1L;
    private static final int SEARCH_RESULT_SIZE = 5000;

    public ArrayList<JobDetailsBO> getJobDetailsBuilder(@NonNull final GetJobMetricsInputBO getJobMetricsInputBO) {

        final QueryBuilder queryBuilder = buildJobDetailsESQuery(getJobMetricsInputBO);
        final SearchRequest searchRequest = buildJobDetailsESSearchRequest(queryBuilder);
        final List<String> jobDetailsJson = elasticSearchAccessor.getRecords(searchRequest);

        final ArrayList<JobDetailsBO> jobDetailsList = new ArrayList<>();
        for (String job : jobDetailsJson) {
            jobDetailsList.add(buildJobDetailsBO(job));
        }
        return jobDetailsList;
    }

    private QueryBuilder buildJobDetailsESQuery(final GetJobMetricsInputBO getJobMetricsInputBO) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(getJobMetricsInputBO.getCity())) {
            queryBuilder = ((BoolQueryBuilder) queryBuilder)
                    .must(new MatchQueryBuilder("cty", getJobMetricsInputBO.getCity()));
        }
        if (StringUtils.isNotEmpty(getJobMetricsInputBO.getMerchantId())) {
            queryBuilder = ((BoolQueryBuilder) queryBuilder)
                    .must(new MatchQueryBuilder("mid", getJobMetricsInputBO.getMerchantId()));
        }

        return queryBuilder;
    }

    private SearchRequest buildJobDetailsESSearchRequest(final QueryBuilder queryBuilder) {

        final SearchRequest searchRequest = new SearchRequest(JOB_ES_INDEX);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(SEARCH_RESULT_SIZE);
        searchSourceBuilder.fetchSource(new String[]{"jid", "mid", "jbd", "lse", "isj", "cty", "jdes"}, null);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(SEARCH_SCROLL_TIMEOUT));

        return searchRequest;
    }

    private JobDetailsBO buildJobDetailsBO(final String jobDetailsJson) {
        final JobDetailsBO jobDetailsBO = gson.fromJson(jobDetailsJson, JobDetailsBO.class);
        return jobDetailsBO;
    }

}
