package org.cris6h16.facades.adresss;

import org.cris6h16.AddressComponent;
import org.cris6h16.facades.AddressFacade;
import org.cris6h16.facades.CreateAddressDTO;
import org.cris6h16.facades.UpdateAddressDTO;
import org.cris6h16.security.SecurityComponent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        availableCountry(dto.getCountry());
        return addressComponent.createAddress(dto);
    }

    private void availableCountry(String country) {
        if (country.trim().equalsIgnoreCase("EC")) return;
        throw new AddressComponentException(AddressComponentErrorCode.UNSUPPORTED_COUNTRY);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAddress(Long id) {
        addressComponent.deleteAddress(id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAddress(UpdateAddressDTO dto, Long id) {
        addressComponent.updateAddress(dto, id);
    }
}
