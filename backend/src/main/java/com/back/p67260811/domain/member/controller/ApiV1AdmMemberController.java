package com.back.p67260811.domain.member.controller;

import com.back.p67260811.domain.member.dto.MemberWithUsernameDto;
import com.back.p67260811.domain.member.service.MemberService;
import com.back.p67260811.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adm/members")
public class ApiV1AdmMemberController {

    private final MemberService memberService;
    private final Rq rq;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "회원 다건 조회")
    public List<MemberWithUsernameDto> getItems() {

        return memberService.findAll().stream()
                .<MemberWithUsernameDto>map(MemberWithUsernameDto::new)
                .toList();
    }
}
