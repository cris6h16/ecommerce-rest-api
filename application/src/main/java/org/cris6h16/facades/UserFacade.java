package org.cris6h16.facades;

import org.cris6h16.user.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

public interface UserFacade {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);
    void verifyEmail(VerifyEmailDTO dto);
    void resetPassword(ResetPasswordDTO dto);

    String refreshAccessToken();

    Page<UserDTO> findAll(Pageable pageable);

    void updateRoles(Long id, Set<String> authorities);


    UserDTO findById(Long id);

    void adjustBalance(Long id, BigDecimal delta);
}
