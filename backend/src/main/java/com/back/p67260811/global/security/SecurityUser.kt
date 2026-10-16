package com.back.p67260811.global.security

import lombok.Getter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

@Getter
class SecurityUser(
    val id: Int?,
    val nickname: String?,
    username: String,
    password: String?,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities) {

}
