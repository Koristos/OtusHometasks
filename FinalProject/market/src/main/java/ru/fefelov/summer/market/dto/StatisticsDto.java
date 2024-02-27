package ru.fefelov.summer.market.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StatisticsDto {
    private final Map<String, Long> serviceWorkingTime = new HashMap<>();
}
