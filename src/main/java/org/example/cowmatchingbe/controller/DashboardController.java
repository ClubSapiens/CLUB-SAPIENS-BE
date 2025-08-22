
package org.example.cowmatchingbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardService.SummaryMetrics> summary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}