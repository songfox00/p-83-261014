package com.back.p67260811.global.initData

import com.back.p67260811.domain.member.service.MemberService
import com.back.p67260811.domain.post.post.service.PostService
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.Transactional

@Configuration
@RequiredArgsConstructor
class BaseInitData(
    @Autowired
    @Lazy
    private val self: BaseInitData,
    private val postService: PostService,
    private val memberService: MemberService

) {
    @Bean
    fun initDataRunner(): ApplicationRunner {
        return ApplicationRunner { args: ApplicationArguments? ->
            self.work1()
            self.work2()
        }
    }

    @Transactional
    fun work2() {

        if (postService.count() > 0) return

        val member1 = memberService.findByUsername("user1").get()
        val member2 = memberService.findByUsername("user2").get()

        postService.write(member1, "제목1", "내용1").apply {
            addComment("댓글 1-1")
            addComment("댓글 1-2")
            addComment("댓글 1-3")
        }

        postService.write(member1, "제목2", "내용2").apply {
            addComment( "댓글 2-1")
            addComment( "댓글 2-2")
        }

        postService.write(member2, "제목3", "내용3")

    }

    @Transactional
    fun work1() {
        if (memberService.count() > 0) {
            return
        }

        memberService.join("system", "system", "시스템", "system")
        memberService.join("admin", "admin", "운영자", "admin")
        memberService.join("user1", "1234", "유저1", "user1")
        memberService.join("user2", "1234", "유저2", "user2")
        memberService.join("user3", "1234", "유저3", "user3")
    }
}