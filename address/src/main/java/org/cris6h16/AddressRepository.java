package org.cris6h16;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    boolean existsByIdAndUserId(Long addressId, Long userId);

    Set<AddressEntity> findByUserId(Long userId);
}
