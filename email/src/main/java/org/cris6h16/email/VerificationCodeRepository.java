package org.cris6h16.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

 interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {

     void deleteByEmailAndActionType(String email, String actionType);

     boolean existsByEmailAndCodeAndUsedAndExpiresAtAfter(String email, String code, boolean used, LocalDateTime localDateTime);

     @Modifying(clearAutomatically = true)
     @Query("UPDATE VerificationCodeEntity v SET v.used = :used WHERE v.email = :email AND v.actionType = :actionType")
     void updateUsedByEmailAndActionType(String email, String actionType, boolean used);
 }
