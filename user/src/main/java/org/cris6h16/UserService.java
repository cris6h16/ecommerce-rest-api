package org.cris6h16;

public interface UserService {
    void signup(SignupInput user);
    LoginOutput login(String email, String password);
}
