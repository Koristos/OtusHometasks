package ru.fefelov.summer.market.events;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Event {

    private EventType type;
    private String userId;
    private String userEmail;
    private Map<String, Double> statistic;
    private Date eventDate = new Date();

}
