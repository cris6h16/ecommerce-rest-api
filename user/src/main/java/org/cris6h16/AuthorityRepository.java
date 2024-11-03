package org.cris6h16;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
     Optional<AuthorityEntity> findByName(String name);
 }
