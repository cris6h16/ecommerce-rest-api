package org.cris6h16.Controllers;

import org.cris6h16.facades.UserDTO;
import org.cris6h16.facades.UserFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

import static org.cris6h16.Controllers.HTTPCommons.PATH_PREFIX;
import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.Controllers.UserController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

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
    public ResponseEntity<UserDTO> me() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(userFacade.me());
    }


    @GetMapping(
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(userFacade.findAll(pageable));
    }

    @PutMapping(
            path = "/{id}/authorities",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> putAuthorities(@PathVariable Long id, @RequestBody Set<String> authorities) {
        userFacade.updateRoles(id, authorities);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(
            path = "/{id}/balance",
            consumes = TEXT_PLAIN_VALUE
    )
    public ResponseEntity<Void> putBalance(@PathVariable Long id, @RequestBody BigDecimal balance) {
        userFacade.updateBalance(id, balance);
        return ResponseEntity.noContent().build();
    }
}
