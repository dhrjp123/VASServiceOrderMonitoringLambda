package model;

import lombok.Value;
import java.util.Date;

@Value
public class GetJobDetailsInput {
    private final String city;
    private final String sellerID;
    private final String serviceCategory;
}
