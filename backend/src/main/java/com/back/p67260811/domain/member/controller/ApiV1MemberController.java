package com.back.p67260811.domain.member.controller;

import com.back.p67260811.domain.member.dto.MemberDto;
import com.back.p67260811.domain.member.entity.Member;
import com.back.p67260811.domain.member.service.MemberService;
import com.back.p67260811.global.dto.RsData;
import com.back.p67260811.global.exception.ServiceException;
import com.back.p67260811.global.rq.Rq;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1MemberController {

    private final MemberService memberService;

    private final Rq rq;

    record JoinReqBody(
            @NotBlank
            @Size(min = 2, max = 30)
            String username,

            @NotBlank
            @Size(min = 2, max = 30)
            String password,

            @NotBlank
            @Size(min = 2, max = 30)
            String nickname
    ) {
    }

    record JoinResBody(
            MemberDto memberDto
    ) {
    }

    @PostMapping("/join")
    public RsData<MemberDto> join(
            @RequestBody @Valid JoinReqBody reqBody
    ) {

        Member member = memberService.join(reqBody.username, reqBody.password, reqBody.nickname);

        return new RsData(
                "201-1",
                "회원가입이 완료되었습니다. %s님 환영합니다.".formatted(reqBody.nickname),
                new JoinResBody(
                        new MemberDto(member)
                )
        );
    }

    record LoginReqBody(
            @NotBlank
            @Size(min = 2, max = 30)
            String username,

            @NotBlank
            @Size(min = 2, max = 30)
            String password
    ) {
    }

    record LoginResBody(
            MemberDto memberDto,
            String apiKey,
            String accessToken
    ) {
    }

    @PostMapping("/login")
    public RsData<MemberDto> login(
            @RequestBody @Valid LoginReqBody reqBody
    ) {

        // 1. 회원 존재 여부
        Member actor = memberService.findByUsername(reqBody.username).orElseThrow(
                () -> new ServiceException("401-1", "존재하지 않는 회원입니다.")
        );
        // 2. 존재하면 비밀 번호 체크
        memberService.checkPassword(reqBody.password, actor.getPassword());
        // 3. 비밀 번호가 맞으면 인증 데이터(apiKey) 제공

        // 4. apiKey 쿠키 생성하고 응답에 포함해서 전송

        String accessToken = memberService.genAccessToken(actor);
        rq.addCookie("apiKey", actor.getApiKey());
        rq.addCookie("accessToken", accessToken);

        return new RsData(
                "200-1",
                "%s님 반갑습니다!".formatted(actor.getNickname()),
                new LoginResBody(
                        new MemberDto(actor),
                        actor.getApiKey(),
                        accessToken
                )
        );
    }

    @DeleteMapping("/logout")
    public RsData<Void> logout() {

        rq.deleteCookie("apiKey");

        return new RsData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }

    @GetMapping("/me")
    public RsData<MemberDto> me() {

        Member actor = rq.getActor(); // 인증
        Member realActor = memberService.findById(actor.getId()).get(); // 인증된 사용자 정보 가져오기

        return new RsData(
                "200-1",
                "OK",
                new MemberDto(realActor)
        );
    }
}