package builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import accessor.ElasticSearchAccessor;
import model.bo.GetJobMetricsInputBO;
import model.bo.JobDetailsBO;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class JobDetailsBuilder {

    private static final Gson gson = new Gson();
    private static final String JOB_ES_INDEX = "jobs";
    private static final long SEARCH_SCROLL_TIMEOUT = 1L;
    private static final int SEARCH_RESULT_SIZE = 5000;
    @NonNull
    private final ElasticSearchAccessor elasticSearchAccessor;

    public List<JobDetailsBO> getJobDetailsBuilder(@NonNull final GetJobMetricsInputBO getJobMetricsInputBO) {

        //final QueryBuilder queryBuilder = buildJobDetailsESQuery(getJobMetricsInputBO);
        //final SearchRequest searchRequest = buildJobDetailsESSearchRequest(queryBuilder);
        //final List<String> jobDetailsJson = elasticSearchAccessor.getRecords(searchRequest);

//        final ArrayList<JobDetailsBO> jobDetailsList = new ArrayList<>();
//        for (String job : jobDetailsJson) {
//            jobDetailsList.add(buildJobDetailsBO(job));
//        }
        return getJobDetailsBOList(getJobMetricsInputBO);
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

    private List<JobDetailsBO> getJobDetailsBOList(GetJobMetricsInputBO getJobMetricsInputBO) {
        Random r = new Random(0);
        List<String> cities = ImmutableList.of("Delhi", "Jaipur", "Hyderabad", "Mumbai", "Shimla", "Manali", "OOty",
                "Patna", "jabalpur", "Khandala");
        List<JobDetailsBO> jobDetailsBOS = new ArrayList<>();
        for(int i=0; i< 100 ;i++) {
            jobDetailsBOS.add(JobDetailsBO.builder()
                    .city(cities.get(r.nextInt(cities.size())))
                    .etaDelay(r.nextInt() %2 == 0)
                    .otaFailure(r.nextInt() %2 == 0)
                    .slotStartTime( String.valueOf(1595788200 + 36000 + 3600 * r.nextInt(10)))
                    .merchantId("MerchantId" + r.nextInt(20))
                    .statusNotUpdated(r.nextInt() %2 == 0 ? "NotServiced" : "Serviced")
                    .technicianId("TechnicianId" + r.nextInt(50))
                    .build());
        }

        return jobDetailsBOS.stream()
                .filter(p -> getJobMetricsInputBO.getCity() == null || getJobMetricsInputBO.getCity().equals(p.getCity()))
                .filter(p -> getJobMetricsInputBO.getMerchantId() == null ||
                        getJobMetricsInputBO.getMerchantId().equals(p.getMerchantId()))
                .collect(Collectors.toList());
    }

    private JobDetailsBO buildJobDetailsBO(final String jobDetailsJson) {
        final JobDetailsBO jobDetailsBO = gson.fromJson(jobDetailsJson, JobDetailsBO.class);
        return jobDetailsBO;
    }

}
