package com.epam.esm.service;

import com.epam.esm.service.impl.CertificateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class DynamicSchedulingConfig implements SchedulingConfigurer {

    private final CertificateServiceImpl certificateService;

    @Autowired
    public DynamicSchedulingConfig(CertificateServiceImpl certificateService){
        this.certificateService = certificateService;
    }

    @Bean
    public Executor taskExecutor(){
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(certificateService::scheduleDeletionOfDetachedTag
                , triggerContext -> {
            Optional<Date> lastCompletionTime =
                    Optional.ofNullable(triggerContext.lastCompletionTime());
            Instant nextExecutionTime =
                    lastCompletionTime.orElseGet(Date::new).toInstant()
                            .plusMillis(2000);
            return Date.from(nextExecutionTime);
        });
    }
}