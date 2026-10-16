package com.back.p67260811.domain.member.dto

import com.back.p67260811.domain.member.entity.Member
import java.time.LocalDateTime


@JvmRecord
data class MemberDto(
    val id: Int?,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val name: String?
) {
    constructor(member: Member) : this(
        member.id,
        member.createDate,
        member.modifyDate,
        member.nickname
    )
}