package ru.fefelov.eventsorter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class BonusEvent {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("statistic")
    private Map<String, Double> statistic;
    @JsonProperty("eventDate")
    private Date eventDate;
}
