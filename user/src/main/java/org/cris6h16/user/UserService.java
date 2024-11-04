package org.cris6h16.user;

public interface UserService {
    Long signup(SignupDTO user);
    LoginOutput login(LoginDTO login);

    void verifyEmail(VerifyEmailDTO dto);
}
