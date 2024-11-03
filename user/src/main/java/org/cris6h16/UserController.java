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

import static org.cris6h16.UserConfig.jsonHeaderCons;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/api/v1/users";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(@RequestBody SignupDTO dto) {
        Long id = userService.signup(dto);
        return ResponseEntity
                .created(URI.create(BASE_PATH + "/" + id))
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

    @PostMapping(
            path = "/verify-email",
            consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> verifyEmail(@RequestBody VerifyEmailDTO dto) {
        userService.verifyEmail(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
