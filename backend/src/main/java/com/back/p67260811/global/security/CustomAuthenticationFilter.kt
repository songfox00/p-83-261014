package com.back.p67260811.global.security

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.member.service.MemberService
import com.back.p67260811.global.exception.ServiceException
import com.back.p67260811.global.rq.Rq
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val memberService: MemberService,
    private val rq: Rq
) : OncePerRequestFilter() {

    companion object {
        private val PUBLIC_PATHS = setOf(
            "/api/v1/members/join",
            "/api/v1/members/login",
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            authenticate(request, response, filterChain)
        } catch (e: ServiceException) {
            val rsData = e.rsData

            response.apply {
                contentType = "application/json; charset=UTF-8"
                status = rsData.statusCode
            }

            response.writer.write(
                """
                    {
                        "resultCode": "${rsData.resultCode}",
                        "msg": "${rsData.msg}"
                    }
                    """.trimIndent()
            )
        }
    }

    private fun authenticate(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val path = request.requestURI

        if (!path.startsWith("/api/") || path in PUBLIC_PATHS) {
            filterChain.doFilter(request, response)
            return
        }

        val (apiKey, accessToken) = getCredentials()

        if (apiKey.isBlank() && accessToken.isBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        if (apiKey.isBlank()) {
            throw ServiceException(
                "401-1",
                "로그인 후 이용해주세요.",
            )
        }

        val tokenMember = accessToken
            .takeIf { it.isNotBlank() }
            ?.let { memberService.payloadOrNull(it) }
            ?.let { payload ->
                val id = (payload["id"] as Number).toInt()
                val username = payload["username"] as String?
                val nickname = payload["nickname"] as String?

                Member(id, username, nickname)
            }

        val member = tokenMember
            ?: memberService.findByApiKey(apiKey).orElseThrow {
                ServiceException(
                    "401-3",
                    "API 키가 유효하지 않습니다.",
                )
            }

        if (accessToken.isNotBlank() && tokenMember == null) {
            val newAccessToken = memberService.genAccessToken(member)

            rq.addCookie("accessToken", newAccessToken)
            rq.setHeader("accessToken", newAccessToken)
        }

        val user = SecurityUser(
            id = member.id,
            nickname = member.nickname,
            username = member.username,
            authorities = member.authorities,
            password = member.password ?: "",
        )

        val authentication = UsernamePasswordAuthenticationToken(
            user,
            null,
            user.authorities,
        )

        val context = SecurityContextHolder.createEmptyContext().apply {
            this.authentication = authentication
        }

        SecurityContextHolder.setContext(context)

        filterChain.doFilter(request, response)
    }

    private fun getCredentials(): Pair<String, String> {
        val authorization = rq.getHeader("Authorization", "")

        if (authorization.isBlank()) {
            return rq.getCookieValue("apiKey", "") to
                    rq.getCookieValue("accessToken", "")
        }

        if (!authorization.startsWith("Bearer ")) {
            throw ServiceException(
                "401-2",
                "Authorization 헤더가 Bearer 형식이 아닙니다.",
            )
        }

        val parts = authorization
            .removePrefix("Bearer ")
            .split(' ', limit = 2)

        val apiKey = parts[0]
        val accessToken = parts.getOrElse(1) { "" }

        return apiKey to accessToken
    }
}