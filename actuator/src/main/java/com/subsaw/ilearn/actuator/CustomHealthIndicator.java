package com.subsaw.ilearn.actuator;

import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {
 
    @Override
    public Health health() {
       // Determine the health of the component
       // For example, to determine the DB status, run a query against the DB to see if it is available
       return Health.up().withDetails(Map.ofEntries(Map.entry("key1", "detail1"), Map.entry("key2", "detail2"))).build();
    }

}
