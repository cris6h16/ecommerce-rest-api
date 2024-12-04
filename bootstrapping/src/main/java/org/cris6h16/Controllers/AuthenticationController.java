package org.cris6h16.Controllers;

import org.cris6h16.facades.EmailFacade;
import org.cris6h16.facades.LoginDTO;
import org.cris6h16.facades.SendEmailVerificationDTO;
import org.cris6h16.facades.SignupDTO;
import org.cris6h16.facades.UserFacade;
import org.cris6h16.facades.VerifyEmailDTO;
import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.cris6h16.Controllers.AuthenticationController.BASE_PATH;
import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping(BASE_PATH)
public class AuthenticationController {
     static final String BASE_PATH = "/api/v1/auth";

    private final UserFacade userFacade;

    public AuthenticationController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(
            path = "/signup",
            consumes = APPLICATION_JSON_VALUE
    )
    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED
    )
    public ResponseEntity<Void> signUp(@RequestBody SignupDTO dto) {
        Long id = userFacade.signup(dto);
        return ResponseEntity
                .created(URI.create(UserController.BASE_PATH + "/me"))
                .build();
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
            path = "/verify-email",
            consumes = APPLICATION_JSON_VALUE
    )
    @Transactional(
            rollbackFor = Exception.class,
            isolation = Isolation.READ_COMMITTED
    )
    public ResponseEntity<Void> verifyEmail(@RequestBody VerifyEmailDTO dto) {
        userFacade.verifyEmail(dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

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
            produces = TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> refreshAccessToken() {
        String accessToken = userFacade.refreshAccessToken(); // todo: organizar mejor aqui no es user facade es authentication facade
        return ResponseEntity.ok(accessToken);
    }



}
