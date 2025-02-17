package org.cris6h16;

import org.cris6h16.Exception.AddressComponentException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static org.cris6h16.Exception.AddressErrorCode.ADDRESS_NOT_FOUND;

@Component
class AddressComponentImpl implements AddressComponent {
    private final AddressRepository addressRepository;
    private final AddressValidator addressValidator;

    AddressComponentImpl(AddressRepository addressRepository, AddressValidator addressValidator) {
        this.addressRepository = addressRepository;
        this.addressValidator = addressValidator;
    }

    @Override
    public Long createAddress(CreateAddressInput input) {
        input.prepare();
        addressValidator.validate(input);
        return addressRepository.save(toEntity(input)).getId();
    }

    private AddressEntity toEntity(CreateAddressInput input) {
        return AddressEntity.builder()
                .street(input.getStreet())
                .city(input.getCity())
                .state(input.getState())
                .country(input.getCountry())
                .mobileNumber(input.getMobileNumber())
                .userId(input.getUserId())
                .reference(input.getReference())
                .build();
    }

    @Override
    public void updateAddress(UpdateAddressInput input, Long addressId) {
        input.prepare();
        addressValidator.validate(input);
        AddressEntity addressEntity = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new AddressComponentException(ADDRESS_NOT_FOUND));
        setInputValuesInEntity(input, addressEntity);
        addressRepository.save(addressEntity);
    }

    private void setInputValuesInEntity(UpdateAddressInput input, AddressEntity entity) {
        entity.setStreet(input.getStreet());
        entity.setCity(input.getCity());
        entity.setState(input.getState());
        entity.setCountry(input.getCountry());
        entity.setMobileNumber(input.getMobileNumber());
        entity.setReference(input.getReference());
        entity.setStreet(input.getStreet());
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public boolean addressBelongsToUser(Long addressId, Long userId) {
        return addressRepository.existsByIdAndUserId(addressId, userId);
    }

    @Override
    public Set<AddressOutput> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::toOutput)
                .collect(Collectors.toSet());
    }

    private AddressOutput toOutput(AddressEntity entity) {
        return AddressOutput.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .country(entity.getCountry())
                .mobileNumber(entity.getMobileNumber())
                .reference(entity.getReference())
                .userId(entity.getUserId())
                .build();
    }
}
