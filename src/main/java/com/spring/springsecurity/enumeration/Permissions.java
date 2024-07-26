package com.spring.springsecurity.enumeration;
public enum Permissions {
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),
    USER_UPDATE("user:update"),
    USER_VIEW("user:view"),

    PROFILE_CREATE("profile:create"),
    PROFILE_DELETE("profile:delete"),
    PROFILE_UPDATE("profile:update"),
    PROFILE_VIEW("profile:view"),

    DEVOTEE_CREATE("devotee:create"),
    DEVOTEE_DELETE("devotee:delete"),
    DEVOTEE_UPDATE("devotee:update"),
    DEVOTEE_VIEW("devotee:view"),

    ROLE_CREATE("role:create"),
    ROLE_DELETE("role:delete"),
    ROLE_UPDATE("role:update"),
    ROLE_VIEW("role:view"),

    PERMISSION_CREATE("permission:create"),
    PERMISSION_DELETE("permission:delete"),
    PERMISSION_UPDATE("permission:update"),
    PERMISSION_VIEW("permission:view"),

    LOGS_CREATE("logs:create"),
    LOGS_DELETE("logs:delete"),
    LOGS_UPDATE("logs:update"),
    LOGS_VIEW("logs:view"),

    MESSAGE_CREATE("message:create"),
    MESSAGE_DELETE("message:delete"),
    MESSAGE_UPDATE("message:update"),
    MESSAGE_VIEW("message:view");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

