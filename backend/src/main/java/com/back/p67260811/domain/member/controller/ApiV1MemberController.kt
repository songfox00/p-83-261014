package com.back.p67260811.domain.member.controller

import com.back.p67260811.domain.member.dto.MemberDto
import com.back.p67260811.domain.member.service.MemberService
import com.back.p67260811.global.dto.RsData
import com.back.p67260811.global.exception.ServiceException
import com.back.p67260811.global.rq.Rq
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/members")
@SecurityRequirement(name = "bearerAuth")
class ApiV1MemberController(
    private val memberService: MemberService,
    private val rq: Rq
) {

    
    data class JoinReqBody(
        val username: @NotBlank @Size(min = 2, max = 30) String,
        val password: @NotBlank @Size(min = 2, max = 30) String,

        val nickname: @NotBlank @Size(min = 2, max = 30) String
    )

    
    data class JoinResBody(
        val memberDto: MemberDto
    )

    @PostMapping("/join")
    fun join(
        @RequestBody @Valid reqBody: @Valid JoinReqBody
    ): RsData<JoinResBody> {
        val member = memberService.join(reqBody.username, reqBody.password, reqBody.nickname)

        return RsData(
            "201-1",
            "회원가입이 완료되었습니다. ${reqBody.nickname}님 환영합니다.",
            JoinResBody(
                MemberDto(member)
            )
        )
    }

    data class LoginReqBody(
        val username: @NotBlank @Size(min = 2, max = 30) String,

        val password: @NotBlank @Size(min = 2, max = 30) String
    )

    data class LoginResBody(
        val memberDto: MemberDto,
        val apiKey: String?,
        val accessToken: String
    )

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid reqBody: @Valid LoginReqBody
    ): RsData<LoginResBody> {

        val actor = memberService.findByUsername(reqBody.username).orElseThrow {
            ServiceException(
                "401-1",
                "존재하지 않는 아이디입니다."
            )
        }
        // 2. 존재하면 비밀 번호 체크
        memberService.checkPassword(reqBody.password, actor.password)

        // 3. 비밀 번호가 맞으면 인증 데이터(apiKey) 제공

        // 4. apiKey 쿠키 생성하고 응답에 포함해서 전송
        val accessToken = memberService.genAccessToken(actor)
        rq.addCookie("apiKey", actor.apiKey!!)
        rq.addCookie("accessToken", accessToken)

        return RsData(
            "200-1",
            "${actor.nickname}님 반갑습니다!",
            LoginResBody(
                MemberDto(actor),
                actor.apiKey,
                accessToken
            )
        )
    }

    @DeleteMapping("/logout")
    fun logout(): RsData<Void?> {
        rq!!.deleteCookie("apiKey")

        return RsData<Void?>(
            "200-1",
            "로그아웃 되었습니다."
        )
    }

    @GetMapping("/me")
    fun me(): RsData<MemberDto> {
        val actor = rq.actor // 인증
        val realActor = memberService.findById(actor.id).get()

        return RsData(
            "200-1",
            "OK",
            MemberDto(realActor)
        )
    }
}