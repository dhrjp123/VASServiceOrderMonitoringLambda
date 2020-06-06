package builder;

import accessor.ElasticSearchAccessor;
import accessor.Temp;
import com.google.gson.Gson;
import lombok.NonNull;
import model.GetJobMetricsInputBO;
import model.JobDetailsBO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobDetailsBuilder {

    private final ElasticSearchAccessor accessor = new ElasticSearchAccessor();

    private JobDetailsBO transformJsonToJavaObject(String jsonJobDetails){
        Gson gson = new Gson();
        final JobDetailsBO jobDetailsBO = gson.fromJson(jsonJobDetails, JobDetailsBO.class);
        return jobDetailsBO;
    }

    public ArrayList<JobDetailsBO> getJobDetailsBuilder(@NonNull GetJobMetricsInputBO input) throws IOException {

        final String index = "jobs";
        final SearchRequest searchRequest = new SearchRequest(index);
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(new MatchQueryBuilder("cty", input.getCity()))
                .must(new MatchQueryBuilder("mid", input.getMerchantID()));

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(5000);
        searchSourceBuilder.fetchSource(new String[]{"jid", "mid", "jbd", "lse", "isj", "cty", "jdes"},null);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(1L));

        /*
        Original
        final List<String> jsonJobDetailsList = accessor.esAccessor(searchRequest);
        */
        //Changed
        Temp temp = new Temp();
        List<String> jsonJobDetailsList = temp.esAccessor(searchRequest);
        //Upto Here

        final ArrayList<JobDetailsBO> jobDetailsList = new ArrayList<>();
        for(String job:jsonJobDetailsList){
            jobDetailsList.add(transformJsonToJavaObject(job));
        }
        return jobDetailsList;
    }

}