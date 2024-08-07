package com.spring.springsecurity.constant;


public class Constant {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String PASSWORD_IS_NOT_CORRECT = "Password is not correct";
    public static final String USER_NOT_FOUND_IN_SECURITY = "Username not found";
    public static final String PASSWORD_EXPIRED = "Password has been expired";
    public static final String ACCOUNT_DISABLED = "Your account is currently disabled";
    public static final String ACCOUNT_EXPIRED = "Account is expired";
    public static final String ACCOUNT_LOCKED = "Account is locked";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String USERNAME_NOT_FOUND = "Username not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_ALREADY_EXISTS = "Role already exists";
    public static final String PERMISSION_NOT_FOUND = "Permission not found";
    public static final String PERMISSION_ALREADY_EXISTS = "Permission already exists";
    public static final String USER_ROLE_NOT_FOUND = "User role not found";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_PREFIX = "AUTHORITY_";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String AUTHORITIES = "authorities";
    public static final String ROLE = "role";

    public static final String USER_AUTHORITIES = "document:read,document:write,document:delete,document:download,document:update";
    public static final String ADMIN_AUTHORITIES = "user:read,user:write,user:delete,user:download,user:update,role:read,role:write,role:delete,role:download,role:update,permission:read,permission:write,permission:delete,permission:download,permission:update,document:read,document:write,document:delete,document:download,document:update";
    public static final String SUPER_ADMIN_AUTHORITIES = "user:read,user:write,user:delete,user:download,user:update,role:read,role:write,role:delete,role:download,role:update,permission:read,permission:write,permission:delete,permission:download,permission:update,document:read,document:write,document:delete,document:download,document:update";
    public static final String EMPTY_VALUE = "empty";
    public static final String EMPTY = "";
    public static final String APPLICATION_LLC = "jamia-akbira";
    public static final String TYPE = "typ";
    public static final String JWT_TYPE = "JWT";
    public static final String RESPONSE_DATA_KEY = "data";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer";

    public static final int NINETY_DAY = 90;
    public static final int PASSWORD_STRENGTH = 12;

}
