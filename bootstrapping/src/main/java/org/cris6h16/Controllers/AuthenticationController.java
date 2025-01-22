package org.cris6h16.Controllers;

import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.facades.UserFacade;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.cris6h16.Controllers.AuthenticationController.BASE_PATH;
import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(BASE_PATH)
public class AuthenticationController {
    static final String BASE_PATH = "/api/v1/auth";

    private final UserFacade userFacade;

    public AuthenticationController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED
    )
    @PostMapping(
            path = "/signup",
            consumes = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> signUp(@RequestBody SignupDTO dto, UriComponentsBuilder ucb) {
        URI uri = ucb.path(UserController.BASE_PATH + "/{id}").
                buildAndExpand(userFacade.signup(dto)).
                toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(
            path = "/login",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED
    )
    public ResponseEntity<LoginOutput> login(@RequestBody LoginDTO dto) {
        LoginOutput output = userFacade.login(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(output);
    }

    // todo: talvez deberia estar en otro endpoint y no en /auth, si se mueve verificar sync con docs


    @PostMapping(
            path = "/reset-password",
            consumes = APPLICATION_JSON_VALUE
    )
    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED
    )
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userFacade.resetPassword(dto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping(
            path = "/refresh-token",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> refreshAccessToken() {
        String accessToken = userFacade.refreshAccessToken(); // todo: organizar mejor aqui no es user facade es authentication facade
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }


}
