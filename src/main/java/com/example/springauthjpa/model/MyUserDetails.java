package com.example.springauthjpa.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetails implements UserDetails {
    private User user;

    Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isAccountexpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isCredentialsexpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountlocked();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
