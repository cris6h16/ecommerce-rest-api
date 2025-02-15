package org.cris6h16.Controllers;

import org.cris6h16.facades.adresss.AddressDTO;
import org.cris6h16.facades.AddressFacade;
import org.cris6h16.facades.CreateAddressDTO;
import org.cris6h16.facades.UpdateAddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

import static org.cris6h16.Controllers.AddressController.ADDRESS_PATH;

@RestController
@RequestMapping(ADDRESS_PATH)
public class AddressController {

    private final AddressFacade addressFacade;
    public static final String ADDRESS_PATH = "/api/v1/address";

    public AddressController(AddressFacade addressFacade) {
        this.addressFacade = addressFacade;
    }

    @PostMapping(
            consumes = "application/json"
    )
    public ResponseEntity<Void> createAddress(@RequestBody CreateAddressDTO dto,
                                              UriComponentsBuilder ucb) {
        URI uri = ucb
                .path(ADDRESS_PATH + "/{id}")
                .buildAndExpand(addressFacade.createAddress(dto))
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(
            path = "/{id}"
    )
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressFacade.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(
            consumes = "application/json",
            path = "/{id}"
    )
    public ResponseEntity<Void> updateAddress(@RequestBody UpdateAddressDTO dto,
                                              @PathVariable Long id) {
        addressFacade.updateAddress(dto, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<Set<AddressDTO>> getMyAddresses() {
        return ResponseEntity
                .ok(addressFacade.getMyAddresses());
    }

}
