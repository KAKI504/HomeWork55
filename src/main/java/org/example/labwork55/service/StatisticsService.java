package org.example.labwork55.service;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getUserStatistics(String userEmail);
    Map<String, Object> getGlobalStatistics();
}