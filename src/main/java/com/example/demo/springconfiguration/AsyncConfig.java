package com.example.demo.springconfiguration;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * Configures a dedicated thread pool for background Excel upload processing.
 *
 * Why a dedicated pool?
 *  - Keeps upload threads separate from Tomcat's request threads.
 *  - Limits concurrent uploads so the DB connection pool is never overwhelmed.
 *  - Allows graceful shutdown: Spring waits up to 60 s for in-progress uploads
 *    to finish before killing the JVM.
 */
@Configuration
@EnableAsync
@EnableScheduling   // required for @Scheduled cron jobs (e.g. CI3 nightly sync)
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * Thread pool used exclusively for Excel bulk-upload tasks.
     *
     * Settings:
     *  corePoolSize  = 2  → always 2 warm threads ready for uploads
     *  maxPoolSize   = 5  → at most 5 concurrent uploads (keep < HikariCP pool size)
     *  queueCapacity = 20 → queue up to 20 pending uploads before rejecting
     */
    @Bean(name = "uploadTaskExecutor")
    Executor uploadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("excel-upload-");
        // Wait for running uploads to finish on application shutdown (max 60 s)
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Logs any exception thrown inside an @Async void method so it is never
     * silently swallowed.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                logger.error("Async method '{}' threw an uncaught exception: {}",
                        method.getName(), ex.getMessage(), ex);
    }

    /**
     * Shared RestTemplate for all outbound HTTP calls (e.g. CI3 sync).
     * Connection timeout : 10 seconds — fail fast if cPanel host is unreachable.
     * Read timeout       : 30 seconds — allow time for CI3 to query its DB and respond.
     */
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(java.time.Duration.ofSeconds(10))
                .setReadTimeout(java.time.Duration.ofSeconds(30))
                .build();
    }
}
