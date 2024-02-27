package ru.fefelov.summer.market.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fefelov.summer.market.dto.StatisticsDto;
import ru.fefelov.summer.market.services.StatisticsService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping
    public StatisticsDto getStatistics() {
        return statisticsService.getStatistics();
    }
}
