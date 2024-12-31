package org.cris6h16.Controllers;

import org.cris6h16.facades.UserDTO;
import org.cris6h16.facades.UserFacade;
import org.cris6h16.facades.VerifyEmailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.Controllers.UserController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping(BASE_PATH)
public class UserController {

    static final String BASE_PATH = "/api/v1/users";


    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
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

    @GetMapping(
            path = "/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(jsonHeaderCons)
                .body(userFacade.findById(id));
    }

    @PatchMapping(
            path = "/{id}/balance",
            consumes = TEXT_PLAIN_VALUE
    )
    public ResponseEntity<Void> adjustBalance(@PathVariable Long id, @RequestBody BigDecimal delta) {
        userFacade.adjustBalance(id, delta);
        return ResponseEntity.noContent().build();
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

    @DeleteMapping(path = "/hard-delete")
    public ResponseEntity<Void> deleteById(@RequestParam String email) {
        userFacade.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }


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
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
