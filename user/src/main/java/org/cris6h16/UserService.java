package org.cris6h16;

public interface UserService {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);

    void verifyEmail(VerifyEmailDTO dto);
}
