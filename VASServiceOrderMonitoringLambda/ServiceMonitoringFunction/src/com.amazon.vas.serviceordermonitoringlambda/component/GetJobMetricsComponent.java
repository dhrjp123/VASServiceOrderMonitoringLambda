package com.amazon.vas.serviceordermonitoringlambda.component;

import com.amazon.vas.serviceordermonitoringlambda.builder.JobDetailsBuilder;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsInputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.GetJobMetricsOutputBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobAggregatedMetricsBO;
import com.amazon.vas.serviceordermonitoringlambda.model.JobDetailsBO;
import com.google.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({ @Inject}))
public class GetJobMetricsComponent {

    @NonNull
    private final JobDetailsBuilder jobDetailsbuilder;

    public GetJobMetricsOutputBO getJobMetrics(@NonNull final GetJobMetricsInputBO getJobMetricsInputBO){
        final List<JobDetailsBO> jobDetailsBOList = jobDetailsbuilder.getJobDetailsBuilder(getJobMetricsInputBO);

        final Function<JobDetailsBO, List<String>> CompositeKey = jobDetailsBO -> {
            return getGroupingCriteriaValues(jobDetailsBO, getJobMetricsInputBO.getGroupingCriteria());
        };

        final Map<List<String>, List<JobDetailsBO>> groupingFunctionJobDetailsMap =
                jobDetailsBOList.stream().collect(Collectors.groupingBy(CompositeKey, Collectors.toList()));

        final Map<List<String>, JobAggregatedMetricsBO> jobMetricsMap = groupingFunctionJobDetailsMap
                .entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey() , e -> calculateJobAggregatedMetrics(e.getValue())));

        final Map<String,Map<String,String>> metaData = jobMetricsMap.entrySet().stream()
                .collect(Collectors.toMap(e-> e.getKey().get(0),e -> getMetaData(e.getKey().get(0)),(value1,value2)->value1));

        return GetJobMetricsOutputBO.builder().metaData(metaData).jobMetricsMap(jobMetricsMap).build();
    }

    private Map<String,String> getMetaData(final String groupingEntity){
        final Map<String,String> groupingEntityMetaData = new HashMap<String, String>(){{
            put("name",groupingEntity);
            put("ID",groupingEntity);
        }};
        return groupingEntityMetaData;
    }
    private JobAggregatedMetricsBO calculateJobAggregatedMetrics(final List<JobDetailsBO> jobDetailsBOList){
        int etaDelayCount = 0;
        int otaFailureCount = 0;
        int statusNotUpdatedCount = 0;
        final int totalJobsCount =  jobDetailsBOList.size();

        for(JobDetailsBO jobDetails : jobDetailsBOList){
            etaDelayCount += (jobDetails.isEtaDelay()?1:0);
            otaFailureCount += (jobDetails.isOtaFailure()?1:0);
            statusNotUpdatedCount += ((jobDetails.getStatusNotUpdated().equals("NotServiced"))?1:0);
        }
        return JobAggregatedMetricsBO.builder().totalJobsCount(totalJobsCount).etaDelayCount(etaDelayCount).otaFailureCount(otaFailureCount).statusNotUpdatedCount(statusNotUpdatedCount).build();
    };

    private List<String> getGroupingCriteriaValues(JobDetailsBO jobDetailsBO, List<String> groupingCriteria){
        final List<String> groupingCriteriaValues = new ArrayList<>();
        for(String criterion : groupingCriteria){
            groupingCriteriaValues.add(getGroupingCriteriaValue(criterion,jobDetailsBO));
        }
        return groupingCriteriaValues;
    }
    private String getGroupingCriteriaValue(String criterion, JobDetailsBO jobDetailsBO) throws RuntimeException{
        String groupingCriteriaValue;
        GroupingCriteria CRITERION = GroupingCriteria.valueOf(criterion);

        switch (CRITERION){
            case city: groupingCriteriaValue = jobDetailsBO.getCity();break;
            case merchantId: groupingCriteriaValue = jobDetailsBO.getMerchantId();break;
            case technicianId: groupingCriteriaValue = jobDetailsBO.getTechnicianID();break;
            case slotStartTime: groupingCriteriaValue = jobDetailsBO.getSlotStartTime();break;
            default :throw new RuntimeException();
        }
        return groupingCriteriaValue;
    }
    private enum GroupingCriteria{
        city,merchantId,technicianId, slotStartTime;
    }

}
