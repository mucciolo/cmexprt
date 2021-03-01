package br.net.comexport.api.core.util;

import br.net.comexport.api.core.entity.Updatable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

import static java.lang.String.format;

public final class ControllerUtils {

    public static final String FMT_NOT_FOUND = "ID %s not found.";
    public static final String FMT_SUCCESSFUL_DELETION = "ID %s successfully deleted.";

    /**
     *
     */
    public static <T extends Updatable<T>, ID, R extends JpaRepository<T, ID>> T updateRepositoryById(
            final R repository,
            final ID id,
            final T updatedEntity)
            throws NoSuchElementException {

        return updatedEntity.update(findInRepositoryById(repository, id));
    }

    public static <T, ID, R extends JpaRepository<T, ID>> T findInRepositoryById(final R repository, final ID id)
            throws NoSuchElementException {

        return repository.findById(id)
                         .orElseThrow(() -> new NoSuchElementException(format(FMT_NOT_FOUND,
                                                                              id)));
    }

    public static <ID, R extends JpaRepository<?, ID>> String deleteFromRepositoryById(final R repository, final ID id)
            throws NoSuchElementException {

        try {
            repository.deleteById(id);

            return format(FMT_SUCCESSFUL_DELETION, id);
        } catch (final EmptyResultDataAccessException | IllegalArgumentException e) {
            throw new NoSuchElementException(format(FMT_NOT_FOUND, id));
        }
    }
}