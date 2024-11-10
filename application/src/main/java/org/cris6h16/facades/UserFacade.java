package org.cris6h16.facades;

import org.cris6h16.user.Outputs.LoginOutput;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.UserOutput;

public interface UserFacade {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);
    void verifyEmail(VerifyEmailDTO dto);
    void resetPassword(ResetPasswordDTO dto);

    UserOutput me();
}
