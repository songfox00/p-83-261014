package com.back.p67260811.domain.member.service

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.member.repository.MemberRepository
import com.back.p67260811.global.exception.ServiceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val authTokenService: AuthTokenService,
    private val passwordEncoder: PasswordEncoder
) {

    fun count(): Long {
        return memberRepository.count()
    }

    fun join(username: String, password: String, nickname: String, apiKey: String? = null): Member {
        var apiKey = apiKey
        if (memberRepository.findByUsername(username).isPresent()) {
            throw ServiceException("409-1", "이미 사용중인 아이디입니다.")
        }

        if (apiKey == null) {
            apiKey = UUID.randomUUID().toString()
        }

        val member = Member(username, passwordEncoder.encode(password), nickname, apiKey)
        return memberRepository.save<Member>(member)
    }

    fun findByUsername(username: String): Optional<Member> {
        return memberRepository.findByUsername(username)
    }

    fun findByApiKey(apiKey: String): Optional<Member> {
        return memberRepository.findByApiKey(apiKey)
    }

    fun genAccessToken(member: Member): String {
        return authTokenService.genAccessToken(member)
    }

    fun payloadOrNull(jwt: String): MutableMap<String, Any>? {
        return authTokenService.payloadOrNull(jwt)
    }

    fun findById(id: Int): Optional<Member> {
        return memberRepository.findById(id)
    }

    fun findAll(): List<Member> {
        return memberRepository.findAll()
    }

    fun checkPassword(inputPassword: String?, rawPassword: String?) {
        if (!passwordEncoder.matches(inputPassword, rawPassword)) {
            throw ServiceException("401-2", "비밀번호가 일치하지 않습니다.")
        }
    }
}