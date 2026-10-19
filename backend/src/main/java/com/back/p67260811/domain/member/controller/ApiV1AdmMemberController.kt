package com.back.p67260811.domain.member.controller

import com.back.p67260811.domain.member.dto.MemberWithUsernameDto
import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import lombok.RequiredArgsConstructor
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adm/members")
class ApiV1AdmMemberController(
    private val memberService: MemberService
) {

    @get:Operation(summary = "회원 다건 조회")
    @get:Transactional(readOnly = true)
    @get:GetMapping
    val items: MutableList<MemberWithUsernameDto>
        get() = memberService.findAll().stream()
            .map { member: Member ->
                MemberWithUsernameDto(
                    member
                )
            }
            .toList()
}
