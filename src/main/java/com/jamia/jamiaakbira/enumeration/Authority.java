package com.jamia.jamiaakbira.enumeration;

import static com.jamia.jamiaakbira.constant.Constant.*;

public enum Authority {
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
    private final String authority;
    Authority(String authority) {
        this.authority = authority;
    }
    public String getAuthority() {
        return authority;
    }
}
