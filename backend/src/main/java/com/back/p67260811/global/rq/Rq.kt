package com.back.p67260811.global.rq

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.member.service.MemberService
import com.back.p67260811.global.exception.ServiceException
import com.back.p67260811.global.security.SecurityUser
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import java.util.function.Predicate

@Component
class Rq(
    private val memberService: MemberService,
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
) {
    val actor: Member
        get() =SecurityContextHolder.getContext()
                ?.authentication
                ?.principal
                ?.let {
                    if(it is SecurityUser) {
                        Member(it.id, it.username,it.nickname)
                    } else {
                        null
                    }
                } ?: throw ServiceException("401-1", "인증 실패")

    fun setHeader(name: String, value: String) {
        response.setHeader(name, value)
    }

    fun getCookieValue(name: String, defaultValue: String): String = request
        .cookies
        .firstOrNull { it.name == name }
        ?.value
        ?.takeIf { it.isNotBlank() }
        ?: defaultValue

    fun addCookie(name: String, value: String) {

        Cookie(name,value).apply{
            domain ="localhost"
            path= "/"
            isHttpOnly = true
        }
            .also{
                response.addCookie(it)
            }

    }

    fun deleteCookie(name: String) {
        Cookie(name, "").apply {
            isHttpOnly=true
            domain = "localhost"
            path = "/"
            maxAge=0
        }.also {
            response.addCookie(it)
        }
    }

    fun getHeader(name: String, defaultValue: String): String =
        request.getHeader(name) ?: defaultValue
}