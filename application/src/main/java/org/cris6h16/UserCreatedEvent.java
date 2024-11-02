package org.cris6h16;

import org.springframework.context.ApplicationEvent;

class UserCreatedEvent  {
    private final String email;

    public UserCreatedEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
