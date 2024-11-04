package org.cris6h16.email;

import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class EmailCommons {
    public static Consumer<HttpHeaders> jsonHeaderCons = h -> h.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
}