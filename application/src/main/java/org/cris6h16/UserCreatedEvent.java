package org.cris6h16;

 class UserCreatedEvent {
    private final String email;w

    public UserCreatedEvent(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
