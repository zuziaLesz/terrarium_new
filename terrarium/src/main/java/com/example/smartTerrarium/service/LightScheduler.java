package com.example.smartTerrarium.service;



import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Component
public class LightScheduler {

    private final TaskScheduler taskScheduler;
    private final RestTemplate restTemplate;

    private ScheduledFuture<?> scheduledTask;

    public LightScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        this.restTemplate = new RestTemplate();
    }

    public void scheduleLightOn(LocalTime targetTime, String urlToCall) {

        // Cancel previous task if it exists
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }

        Runnable task = () -> {
            try {
                System.out.println("Executing scheduled light task at " + LocalDateTime.now());
                restTemplate.postForObject(urlToCall, null, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Build next execution time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = LocalDateTime.of(now.toLocalDate(), targetTime);

        // If time already passed today → schedule tomorrow
        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }

        scheduledTask = taskScheduler.schedule(
                task,
                Date.from(nextRun.atZone(ZoneId.systemDefault()).toInstant())
        );

        System.out.println("Light ON scheduled for: " + nextRun);
    }
}