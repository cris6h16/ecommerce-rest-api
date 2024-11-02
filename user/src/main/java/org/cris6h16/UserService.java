package org.cris6h16;

interface UserService {
    UserOutput createUser(CreateUserInput user);
    UserOutput login(String email, String password);
    UserOutput getUserByEmail(String email);
}
