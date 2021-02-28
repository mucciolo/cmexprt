package br.net.comexport.api.core.util;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

import static java.lang.String.format;

public final class ControllerUtils {

    public static final String FMT_NOT_FOUND = "ID %s not found.";
    private static final String FMT_SUCCESSFUL_DELETION = "ID %s successfully deleted.";

    public static <T, U> T findInRepositoryById(final JpaRepository<T, U> repository, final U id)
            throws NoSuchElementException {
        return repository.findById(id)
                         .orElseThrow(() -> new NoSuchElementException(format(FMT_NOT_FOUND,
                                                                              id)));
    }

    public static <T> String deleteFromRepositoryById(final JpaRepository<?, T> repository, final T id)
            throws NoSuchElementException {
        try {
            repository.deleteById(id);
            return format(FMT_SUCCESSFUL_DELETION, id);
        } catch (final EmptyResultDataAccessException | IllegalArgumentException e) {
            throw new NoSuchElementException(format(FMT_NOT_FOUND, id));
        }
    }
}