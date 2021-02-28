package br.net.comexport.api.core.auditor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public DateAuditor auditorProvider() {
        return new DateAuditor();
    }
}