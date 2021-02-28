package br.net.comexport.api.core.entity;

public interface Updatable<SELF> {
    SELF update(final SELF entityToBeUpdate);
}
