package org.cris6h16.product;

interface Prepareable {
    void trim();

    void nullToEmpty();

    default void prepare() {
        nullToEmpty();
        trim();
    }
}
