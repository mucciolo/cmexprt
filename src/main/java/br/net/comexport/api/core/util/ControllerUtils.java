package br.net.comexport.api.core.util;

import br.net.comexport.api.core.entity.Updatable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

import static java.lang.String.format;

public final class ControllerUtils {

    public static final String FMT_NOT_FOUND = "ID %s not found.";
    public static final String FMT_SUCCESSFUL_DELETION = "ID %s successfully deleted.";

    public static <T, ID> T findInRepositoryById(final JpaRepository<T, ID> repository, final ID id)
            throws NoSuchElementException {

        return repository.findById(id)
                         .orElseThrow(() -> new NoSuchElementException(format(FMT_NOT_FOUND,
                                                                              id)));
    }

    /**
     * @implNote use under @Transactional
     */
    public static <T extends Updatable<T>, ID> T updateRepositoryById(final JpaRepository<T, ID> repository,
                                                                       final ID id,
                                                                       final T updatedEntity)
            throws NoSuchElementException {

        return updatedEntity.update(findInRepositoryById(repository, id));
    }

    public static <ID> String deleteFromRepositoryById(final JpaRepository<?, ID> repository, final ID id)
            throws NoSuchElementException {

        try {
            repository.deleteById(id);

            return format(FMT_SUCCESSFUL_DELETION, id);
        } catch (final EmptyResultDataAccessException | IllegalArgumentException e) {
            throw new NoSuchElementException(format(FMT_NOT_FOUND, id));
        }
    }
}