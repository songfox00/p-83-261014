package com.back.p67260811.domain.member.service

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.standard.Ut
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Map

@Service
class AuthTokenService(
    @Value("\${custom.jwt.secret-key}")
    private val secretPattern: String,

    @Value("\${custom.jwt.expireMills}")
    private val expireMills: Long = 0
) {

    fun genAccessToken(member: Member): String {
        return Ut.jwt.toString(
            secretPattern,
            expireMills,
            Map.of<String, Any>("id", member.id, "username", member.username, "nickname", member.nickname)
        )
    }

    fun payloadOrNull(jwt: String): MutableMap<String, Any>? {
        val payload = Ut.jwt.payloadOrNull(jwt, secretPattern)

        return payload?.let{
            val id = payload.get("id") as Int
            val username = payload.get("username") as String
            val nickname = payload.get("nickname") as String
            Map.of<String, Any>("id", id, "username", username, "nickname", nickname)
        }
    }
}