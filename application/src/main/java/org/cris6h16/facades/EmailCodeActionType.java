package org.cris6h16.facades;

public enum EmailCodeActionType {
    RESET_PASSWORD, VERIFY_EMAIL;

    public static boolean contains(String actionType) {
        for (EmailCodeActionType t : EmailCodeActionType.values()) {
            if (t.name().equals(actionType)) return true;
        }
        return false;
    }
}

