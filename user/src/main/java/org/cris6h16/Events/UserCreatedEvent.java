package org.cris6h16.Events;

public class UserCreatedEvent {
    private final String email;

    public UserCreatedEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
