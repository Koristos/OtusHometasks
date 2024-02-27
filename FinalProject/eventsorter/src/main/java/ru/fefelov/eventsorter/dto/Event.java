package ru.fefelov.eventsorter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Event {

    @JsonProperty("type")
    private EventType type;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("userEmail")
    private String userEmail;
    @JsonProperty("statistic")
    private Map<String, Double> statistic;
    @JsonProperty("eventDate")
    private Date eventDate;

}
