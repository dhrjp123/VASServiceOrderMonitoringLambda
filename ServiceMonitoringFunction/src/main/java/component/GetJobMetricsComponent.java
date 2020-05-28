package component;

import builder.JobDetailsBuilder;
import model.*;

import java.io.IOException;
import java.util.*;

public class GetJobMetricsComponent {

    private List<String> groupingCriteria;
    int etaDelayCount;
    int otaFailureCount;
    int statusNotUpdatedCount;
    int totalJobCount;


    Map<String,JobAggregatedMetrics> jobAggregatedMetricsMap;
    List<JobAggregatedMetricsRow> jobAggregatedMetricsRowList;

    /*Comparator for sorting the list */
    /***************/
    private class groupByComparator implements Comparator<JobDetails> {
        @Override
        public int compare(JobDetails jobA, JobDetails jobB) {

            String groupingEntity1stDimJobA = getGroupingEntity(groupingCriteria.get(0),jobA).toLowerCase();
            String groupingEntity1stDimJobB = getGroupingEntity(groupingCriteria.get(0),jobB).toLowerCase();

            String groupingEntity2ndDimJobA = getGroupingEntity(groupingCriteria.get(1),jobA).toLowerCase();
            String groupingEntity2ndDimJobB = getGroupingEntity(groupingCriteria.get(1),jobB).toLowerCase();

            //ascending order of entity1 and entity2
            if(groupingEntity1stDimJobA.equals(groupingEntity1stDimJobB))
                return groupingEntity2ndDimJobA.compareTo(groupingEntity2ndDimJobB);
            return groupingEntity1stDimJobA.compareTo(groupingEntity1stDimJobB);
        }
    };
    /*****************/

    /*getGroupingEntity*/
    private String getGroupingEntity(String groupBy, JobDetails jobDetails){
        switch (groupBy){
            case "city":return jobDetails.getCity();
            case "sellerID":return jobDetails.getSellerID();
            case "technicianID": return jobDetails.getTechnicianID();
            case "time" : return jobDetails.getTime();
            case "serviceCategory": return jobDetails.getServiceCategory();
            default :return null;
        }
    }
    /****/

    /*Get Name and Id of entities*/

    private String getId(String groupBy, JobDetails jobDetails){
        switch (groupBy){
            case "sellerID": return jobDetails.getSellerID();
            case "technicianID": return jobDetails.getTechnicianID();
            default:return null;
        }
    }
    private String getName(String groupBy, JobDetails jobDetails){
        switch (groupBy){
            case "city": return jobDetails.getCity();
            case "sellerID": return jobDetails.getSellerName();
            case "technicianID": return jobDetails.getTechnicianName();
            default:return null;
        }
    }

    /****/

    private JobAggregatedMetricsRow createJobAggregatedMetricsRow(JobDetails job){
        Map<String,String> entityMetaData = new HashMap<>();
        entityMetaData.put("EntityId",getId(groupingCriteria.get(0),job));
        entityMetaData.put("EntityName",getName(groupingCriteria.get(0),job));

        JobAggregatedMetricsRow jobAggregatedMetricsRow = new JobAggregatedMetricsRow(entityMetaData,jobAggregatedMetricsMap);
        return jobAggregatedMetricsRow;
    }

    public GetJobMetricsOutput getJobMetrics(GetJobMetricsInput input) throws IOException {

        groupingCriteria = input.getGroupingCriteria();
        JobDetailsBuilder builder = new JobDetailsBuilder();
        List<JobDetails> jobDetailsList = builder.GetJobDetailsBuilder(input);

        /*Sorting*/
        Collections.sort(jobDetailsList,new groupByComparator());

        /*Grouping */

        int n = jobDetailsList.size();
        int i,j;
        jobAggregatedMetricsRowList = new ArrayList<>();

        for(i=0;i<n;){

            JobDetails baseJob = jobDetailsList.get(i);
            String baseGroupingEntity1stDim = getGroupingEntity(groupingCriteria.get(0),baseJob);
            String baseGroupingEntity2ndDim = getGroupingEntity(groupingCriteria.get(1),baseJob);

            jobAggregatedMetricsMap = new HashMap<>();

            etaDelayCount = 0;
            otaFailureCount = 0;
            statusNotUpdatedCount = 0;

            for(j=i;j<n;j++){

                JobDetails compJob = jobDetailsList.get(j);
                String compGroupingEntity1stDim = getGroupingEntity(groupingCriteria.get(0),compJob);
                String compGroupingEntity2ndDim = getGroupingEntity(groupingCriteria.get(1),compJob);

                if(!(baseGroupingEntity2ndDim.equals(compGroupingEntity2ndDim))  || !(baseGroupingEntity1stDim.equals(compGroupingEntity1stDim))) {

                    totalJobCount = etaDelayCount+otaFailureCount+statusNotUpdatedCount;
                    JobAggregatedMetrics jobAggregatedMetrics = new JobAggregatedMetrics(totalJobCount,statusNotUpdatedCount ,otaFailureCount,etaDelayCount);
                    jobAggregatedMetricsMap.put(baseGroupingEntity2ndDim,jobAggregatedMetrics);

                    if(!(baseGroupingEntity1stDim.equals(compGroupingEntity1stDim))){
                        jobAggregatedMetricsRowList.add(createJobAggregatedMetricsRow(baseJob));
                    }

                    break;
                }

                etaDelayCount += (compJob.isEtaDelay()?1:0);
                otaFailureCount += (compJob.isOtaFailure()?1:0);
                statusNotUpdatedCount += ((compJob.getStatusNotUpdated().equals("NotServiced"))?1:0);

            }
            i = j;
            if(i == n)
                jobAggregatedMetricsRowList.add(createJobAggregatedMetricsRow(baseJob));
        }


        GetJobMetricsOutput out = new GetJobMetricsOutput(jobAggregatedMetricsRowList);
        return out;
    }
}
