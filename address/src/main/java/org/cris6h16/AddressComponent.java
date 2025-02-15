package org.cris6h16;

import java.util.Set;

public interface AddressComponent {

    Long createAddress(CreateAddressInput input);

    void updateAddress(UpdateAddressInput input, Long addressId);

    void deleteAddress(Long addressId);

    boolean addressBelongsToUser(Long addressId, Long userId);

    Set<AddressOutput> getAddressesByUserId(Long userId);
}