package org.cris6h16.Controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/tests")
@Profile("load-data")
public class TestController {
    private final JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/reset-functional-testing-db")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void resetFunctionalTestingDb() {
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("data-testing.sql").toURI());
            String sql = Files.readString(path);
            jdbcTemplate.execute(sql);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to reset the functional testing database", e);
        }
    }
}
