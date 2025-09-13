package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.dto.SummaryDto;
import org.example.cowmatchingbe.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final UserRepository userRepository;

    public SummaryDto getSummary() {
        long total = userRepository.count();
        return new SummaryDto(total);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }
}