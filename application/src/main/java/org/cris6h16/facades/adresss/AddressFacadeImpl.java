package org.cris6h16.facades.adresss;

import org.cris6h16.AddressComponent;
import org.cris6h16.CreateAddressInput;
import org.cris6h16.facades.AddressFacade;
import org.cris6h16.facades.CreateAddressDTO;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.facades.UpdateAddressDTO;
import org.cris6h16.security.SecurityComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.UNSUPPORTED_COUNTRY;

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
        availableCountry(dto.getCountry());
        return addressComponent.createAddress(toInput(dto));
    }

    private CreateAddressInput toInput(CreateAddressDTO dto) {
        return null;
    }

    private void availableCountry(String country) {
        if (country.trim().equalsIgnoreCase("EC")) return;
        throw new ApplicationException(UNSUPPORTED_COUNTRY);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAddress(Long id) {
//        addressComponent.deleteAddress(id);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateAddress(UpdateAddressDTO dto, Long id) {
//        addressComponent.updateAddress(dto, id);
    }
}
