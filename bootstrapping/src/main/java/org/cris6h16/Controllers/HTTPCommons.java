package org.cris6h16.Controllers;

import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HTTPCommons {
    public static final Consumer<HttpHeaders> jsonHeaderCons = h -> h.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    public static final String PATH_PREFIX = "/api/v1"; // todo: pasar a yaml
}