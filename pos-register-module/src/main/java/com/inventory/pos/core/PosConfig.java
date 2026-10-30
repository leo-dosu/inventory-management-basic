package com.inventory.pos.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PosConfig {

    /** Injected wherever "today" matters (due dates, overdue status) so tests can pin the date. */
    @Bean
    @ConditionalOnMissingBean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
