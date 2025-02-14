package org.cris6h16.facades;

public interface AddressFacade {
    Long createAddress(CreateAddressDTO dto);

    void deleteAddress(Long id);

    void updateAddress(UpdateAddressDTO dto, Long id);
}
