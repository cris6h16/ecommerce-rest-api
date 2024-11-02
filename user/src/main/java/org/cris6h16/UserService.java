package org.cris6h16;

public interface UserService {
    UserOutput createUser(CreateUserInput user);
    UserOutput findByEmail(String email);
}
