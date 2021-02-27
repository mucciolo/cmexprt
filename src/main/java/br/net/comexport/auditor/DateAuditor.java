package br.net.comexport.auditor;

import org.springframework.data.domain.AuditorAware;

import java.util.Date;
import java.util.Optional;

import static java.lang.System.currentTimeMillis;

public class DateAuditor implements AuditorAware<Date> {

    @Override
    public Optional<Date> getCurrentAuditor() {
        return Optional.of(new Date(currentTimeMillis()));
    }
}