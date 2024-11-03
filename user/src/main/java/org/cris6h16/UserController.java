package org.cris6h16;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Consumer;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    public static Consumer<HttpHeaders> jsonHeaderCons = h -> h.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Void> signUp(@RequestBody SignupDTO dto) {
        Long id = userService.signup(dto);
        return ResponseEntity
                .created(URI.create("/users/" + id))
                .build();
    }

    @PostMapping(
            path = "/login",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoginOutput> login(@RequestBody LoginDTO dto) {
        LoginOutput output = userService.login(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(output);
    }
}
