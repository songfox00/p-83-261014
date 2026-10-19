package com.back.p67260811.domain.member.entity

import com.back.p67260811.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@NoArgsConstructor
@Entity
class Member(
    @Column(unique = true)
    var username: String,
    var password: String?,
    var nickname: String?,
    @Column(unique = true)
    var apiKey: String?
) : BaseEntity() {

    constructor(id: Int, username: String, nickname: String?)
            : this(username,null, nickname, null) {
        this.id = id
        this.username = username
        this.nickname = nickname
    }

    constructor(username: String, password: String?, nickname: String?)
        : this(username, password, nickname, null) {
        this.username = username
        this.password = password
        this.nickname = nickname
    }

    val isAdmin: Boolean
        get() = "admin" == username

    val authorities: MutableCollection<out GrantedAuthority>
        get() {
            val authorities: MutableList<GrantedAuthority> =
                ArrayList()

            if (this.isAdmin) {
                authorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
            }

            return authorities
        }
}
