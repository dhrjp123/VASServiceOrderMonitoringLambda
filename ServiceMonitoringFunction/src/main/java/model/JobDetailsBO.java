package model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class JobDetailsBO {
    @SerializedName("jdes")
    private final String statusNotUpdated;

    @SerializedName("isj")
    private final boolean otaFailure;

    @SerializedName("lse")
    private final boolean etaDelay;

    @SerializedName("cty")
    private final String city;

    @SerializedName("mid")
    private final String merchantID;

    @SerializedName("tid")
    private final String technicianID;

    @SerializedName("jbd")
    private final String time;

}
