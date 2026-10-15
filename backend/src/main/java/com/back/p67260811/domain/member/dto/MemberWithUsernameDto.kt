package com.back.p67260811.domain.member.dto

import com.back.p67260811.domain.member.entity.Member
import java.time.LocalDateTime

@JvmRecord
data class MemberWithUsernameDto(
    val id: Int,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val username: String,
    val nickname: String
) {
    constructor(member: Member) : this(
        member.id,
        member.createDate,
        member.modifyDate,
        member.getUsername(),
        member.getNickname()
    )
}