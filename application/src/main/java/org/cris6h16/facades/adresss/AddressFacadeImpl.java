package org.cris6h16.facades.adresss;

import org.cris6h16.AddressComponent;
import org.cris6h16.AddressOutput;
import org.cris6h16.CreateAddressInput;
import org.cris6h16.UpdateAddressInput;
import org.cris6h16.facades.AddressFacade;
import org.cris6h16.facades.CreateAddressDTO;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.facades.UpdateAddressDTO;
import org.cris6h16.security.SecurityComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.ADDRESS_NOT_FOUND;

@Service
class AddressFacadeImpl implements AddressFacade {
    private final AddressComponent addressComponent;
    private final SecurityComponent securityComponent;

    AddressFacadeImpl(AddressComponent addressComponent, SecurityComponent securityComponent) {
        this.addressComponent = addressComponent;
        this.securityComponent = securityComponent;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Long createAddress(CreateAddressDTO dto) {
        Long userId = securityComponent.getCurrentUserId();
        return addressComponent.createAddress(toInput(dto, userId));
    }

    private CreateAddressInput toInput(CreateAddressDTO dto, Long userId) {
        return CreateAddressInput.builder()
                .city(dto.getCity())
                .country("Ecuador")
                .mobileNumber(dto.getMobileNumber())
                .state(dto.getState())
                .street(dto.getStreet())
                .userId(userId)
                .reference(dto.getReference())
                .build();
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAddress(Long addressId) {
        addressBelongsToCurrentUser(addressId);
        addressComponent.deleteAddress(addressId);
    }

    private void addressBelongsToCurrentUser(Long addressId) {
        Long userId = securityComponent.getCurrentUserId();
        if (!addressComponent.addressBelongsToUser(addressId, userId)) {
            throw new ApplicationException(ADDRESS_NOT_FOUND);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAddress(UpdateAddressDTO dto, Long addressId) {
        addressBelongsToCurrentUser(addressId);
        addressComponent.updateAddress(toInput(dto), addressId);
    }

    @Override
    public Set<AddressDTO> getMyAddresses() {
        Long userId = securityComponent.getCurrentUserId();
        return addressComponent
                .getAddressesByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    private AddressDTO toDTO (AddressOutput output){
        return AddressDTO.builder()
                .id(output.getId())
                .city(output.getCity())
                .country(output.getCountry())
                .mobileNumber(output.getMobileNumber())
                .state(output.getState())
                .street(output.getStreet())
                .reference(output.getReference())
                .userId(output.getUserId())
                .build();
    }

    private UpdateAddressInput toInput(UpdateAddressDTO dto) {
        return UpdateAddressInput.builder()
                .city(dto.getCity())
                .country(dto.getCountry())
                .mobileNumber(dto.getMobileNumber())
                .state(dto.getState())
                .street(dto.getStreet())
                .build();
    }
}
