package component;

import builder.JobDetailsBuilder;
import lombok.NonNull;
import model.GetJobMetricsInputBO;
import model.GetJobMetricsOutputBO;
import model.JobAggregatedMetricsBO;
import model.JobDetailsBO;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetJobMetricsComponent {

    private final JobDetailsBuilder jobDetailsbuilder = new JobDetailsBuilder();

    private Map<String,String> getMetaData(String groupingEntity){
        final Map<String,String> groupingEntityMetaData = new HashMap<String, String>(){{
            put("name",groupingEntity);
            put("ID",groupingEntity);
        }};
        return groupingEntityMetaData;
    }
    private JobAggregatedMetricsBO calculateAggregateMetrics(List<JobDetailsBO> unitJobDetailsBOList){
        int etaDelayCount = 0;
        int otaFailureCount = 0;
        int statusNotUpdatedCount = 0;
        int totalJobsCount;

        for(JobDetailsBO jobDetails : unitJobDetailsBOList){
            etaDelayCount += (jobDetails.isEtaDelay()?1:0);
            otaFailureCount += (jobDetails.isOtaFailure()?1:0);
            statusNotUpdatedCount += ((jobDetails.getStatusNotUpdated().equals("NotServiced"))?1:0);
        }
        totalJobsCount = etaDelayCount + otaFailureCount + statusNotUpdatedCount;

        return new JobAggregatedMetricsBO(totalJobsCount,statusNotUpdatedCount ,otaFailureCount,etaDelayCount);
    };

    private List<String> getGroupingCriteriaValues(JobDetailsBO jobDetailsBO, List<String> groupingCriteria) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<String> groupingCriteriaValues = new ArrayList<>();
        for(String criterion : groupingCriteria){
            criterion = criterion.substring(0,1).toUpperCase()+criterion.substring(1);
            groupingCriteriaValues.add(JobDetailsBO.class.getMethod("get" + criterion ).invoke(jobDetailsBO).toString());
        }
        return groupingCriteriaValues;

    }
    public GetJobMetricsOutputBO getJobMetrics(@NonNull GetJobMetricsInputBO input) throws IOException {
        final List<JobDetailsBO> jobDetailsBOList = jobDetailsbuilder.getJobDetailsBuilder(input);

        final Function<JobDetailsBO, List<String>> CompositeKey = jobDetailsBO -> {
            try {
                return getGroupingCriteriaValues(jobDetailsBO,input.getGroupingCriteria());
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        };

        final Map<List<String>, List<JobDetailsBO>> groupingFunctionJobDetailsMap =
                    jobDetailsBOList.stream().collect(Collectors.groupingBy(CompositeKey, Collectors.toList()));

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = groupingFunctionJobDetailsMap
                .entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey() , e -> calculateAggregateMetrics(e.getValue())));

        final Map<String,Map<String,String>> metaData = jobMetricsMap.entrySet().stream()
                .collect(Collectors.toMap(e-> e.getKey().get(0),e -> getMetaData(e.getKey().get(0)),(value1,value2)->value1));

        return new GetJobMetricsOutputBO(metaData,jobMetricsMap);
    }
}