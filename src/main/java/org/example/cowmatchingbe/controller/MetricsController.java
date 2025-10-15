package org.example.cowmatchingbe.controller;
import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.dto.SummaryDto;
import org.example.cowmatchingbe.service.MetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService metricsService;


    @GetMapping("/api/metrics/summary")
    public SummaryDto getSummary() {
        return metricsService.getSummary(); // { "totalUsers": 123 }
    }


    @GetMapping("/api/metrics/users/count")
    public Map<String, Long> getUsersCount() {
        return Map.of("count", metricsService.getTotalUsers());
    }
}