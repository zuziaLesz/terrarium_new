package com.example.smartTerrarium.service;



import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
public class LightScheduler {
    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledTask;

    public LightScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // Schedule a task dynamically
    public void scheduleDailyTask(int hour, int minute, Runnable task) {
        // Cancel previous task if it exists
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }

        // Calculate next execution time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0);

        // If time already passed today, schedule for tomorrow
        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }

        scheduledTask = taskScheduler.schedule(
                task,
                Date.from(nextRun.atZone(ZoneId.systemDefault()).toInstant())
        );
    }
}
