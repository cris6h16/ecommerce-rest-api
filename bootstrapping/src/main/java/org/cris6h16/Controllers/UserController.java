package org.cris6h16.Controllers;

import org.cris6h16.facades.UserFacade;
import org.cris6h16.user.UserOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/api/v1/users";

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }


    @GetMapping(
            path = "/me",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserOutput> me() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userFacade.me());
    }
}
