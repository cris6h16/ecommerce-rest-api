package org.cris6h16.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
     Optional<AuthorityEntity> findByName(String name);
 }
