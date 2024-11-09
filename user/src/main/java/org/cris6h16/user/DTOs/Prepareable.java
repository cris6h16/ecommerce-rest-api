package org.cris6h16.user.DTOs;

 interface Prepareable {
     void trim();

    void nullToEmpty();

    default void prepare() {
        nullToEmpty();
        trim();
    }
}
