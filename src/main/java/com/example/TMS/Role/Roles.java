package com.example.TMS.Role;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    Администратор, Пользователь;
    @Override
    public String getAuthority() { return name(); }
}
