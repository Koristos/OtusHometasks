package ru.fefelov.summer.market.services;

import org.springframework.stereotype.Component;
import ru.fefelov.summer.market.dto.StatisticsDto;
import ru.fefelov.summer.market.utils.StatisticsWatcher;

import java.util.Map;

@Component
public class StatisticsService {
    public StatisticsDto getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        Map<String, Long> statistics = StatisticsWatcher.getStatistics();
        statistics.forEach((key, value) -> {
            String[] nameParts = key.split("\\.");
            statisticsDto.getServiceWorkingTime()
                    .put(nameParts[nameParts.length - 1], value);
        });
        return statisticsDto;
    }
}
