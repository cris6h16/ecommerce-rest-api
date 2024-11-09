package org.cris6h16.user;

import org.cris6h16.user.DTOs.LoginDTO;
import org.cris6h16.user.DTOs.ResetPasswordDTO;
import org.cris6h16.user.DTOs.SignupDTO;
import org.cris6h16.user.DTOs.VerifyEmailDTO;
import org.cris6h16.user.Outputs.LoginOutput;

public interface UserService {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);
    void verifyEmail(VerifyEmailDTO dto);
    void resetPassword(ResetPasswordDTO dto);
}
