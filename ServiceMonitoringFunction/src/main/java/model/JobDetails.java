package model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JobDetails {
    @SerializedName("jdes")
    private String statusNotUpdated;

    @SerializedName("isj")
    private boolean otaFailure;

    @SerializedName("lse")
    private boolean etaDelay;

    @SerializedName("cty")
    private String city;

    @SerializedName("mid")
    private String sellerID;

    @SerializedName("mname")
    private String sellerName;

    @SerializedName("tid")
    private String technicianID;

    @SerializedName("tname")
    private String technicianName;

    @SerializedName("jbd")
    private String time;

    @SerializedName("asn")
    private String serviceCategory;

}
