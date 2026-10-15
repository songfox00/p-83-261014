package com.back.p67260811.domain.post.comment.dto

import com.back.p67260811.domain.post.comment.entity.PostComment
import java.time.LocalDateTime

@JvmRecord
data class PostCommentDto private constructor(
    val id: Int,
    val content: String,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime
) {
    constructor(postComment: PostComment) : this(
        postComment.id,
        postComment.getContent(),
        postComment.createDate,
        postComment.modifyDate
    )
}
