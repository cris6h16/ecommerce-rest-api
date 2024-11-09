package org.cris6h16.facades;

import org.cris6h16.user.LoginDTO;
import org.cris6h16.user.ResetPasswordDTO;
import org.cris6h16.user.SignupDTO;
import org.cris6h16.user.VerifyEmailDTO;
import org.cris6h16.user.Outputs.LoginOutput;

public interface UserFacade {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);
    void verifyEmail(VerifyEmailDTO dto);
    void resetPassword(ResetPasswordDTO dto);
}
