package com.jamia.jamiaakbira.enumeration;

public enum Roles {
    SUPER_ADMIN("SUPER_ADMIN"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER"),
    TEACHER("TEACHER");
    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
