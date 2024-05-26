package com.ems.common.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EmsScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmsScheduler.class);

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void scheduleCronJob() {
        LOGGER.info("EmsScheduler is successfully running...");
    }
}
