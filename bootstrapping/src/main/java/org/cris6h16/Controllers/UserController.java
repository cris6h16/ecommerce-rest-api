package org.cris6h16.Controllers;

import org.cris6h16.facades.UserFacade;
import org.cris6h16.user.UserOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;
import static org.cris6h16.Controllers.UserController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(BASE_PATH)
public class UserController {

     static final String BASE_PATH = PATH_PREFIX + "/users";


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
