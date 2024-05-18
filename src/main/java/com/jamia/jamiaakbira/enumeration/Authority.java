package com.jamia.jamiaakbira.enumeration;

import com.jamia.jamiaakbira.constant.Constant;

import static com.jamia.jamiaakbira.constant.Constant.USER_AUTHORITIES;
import static com.jamia.jamiaakbira.constant.Constant.ADMIN_AUTHORITIES;
import static com.jamia.jamiaakbira.constant.Constant.SUPER_ADMIN_AUTHORITIES;

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
