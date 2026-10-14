package com.back.p67260811.global.security;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class SecurityUser extends User {

    private int id;
    private String nickname;

    public SecurityUser(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities, int id, String nickname) {
        super(username, password, authorities);
        this.id = id;
        this.nickname = nickname;
    }

    public SecurityUser(String username, @Nullable String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
