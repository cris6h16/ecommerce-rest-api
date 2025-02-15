package org.cris6h16.facades;

import org.cris6h16.facades.adresss.AddressDTO;

import java.util.Set;

public interface AddressFacade {
    Long createAddress(CreateAddressDTO dto);

    void deleteAddress(Long addressId);

    void updateAddress(UpdateAddressDTO dto, Long addressId);

    Set<AddressDTO> getMyAddresses();
}
