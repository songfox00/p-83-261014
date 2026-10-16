package com.back.p67260811.domain.post.post.dto

import com.back.p67260811.domain.post.post.entity.Post
import java.time.LocalDateTime

@JvmRecord
data class PostDto private constructor(
    val id: Int?,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val title: String,
    val content: String,
    val nickname: String?,
    val username: String
) {
    constructor(post: Post) : this(
        post.id,
        post.createDate,
        post.modifyDate,
        post.title,
        post.content,
        post.author.nickname,
        post.author.username
    )
}
