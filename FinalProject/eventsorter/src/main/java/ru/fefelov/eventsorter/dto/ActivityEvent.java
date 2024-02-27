package ru.fefelov.eventsorter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityEvent {

    @JsonProperty("user_guid")
    private String userId;

    @JsonProperty("active_date")
    private LocalDateTime eventDate;
}
