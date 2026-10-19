package com.back.p67260811.domain.post.comment.entity

import com.back.p67260811.domain.post.post.entity.Post
import com.back.p67260811.global.jpa.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne

@Entity
class PostComment(
    var content: String,
    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post
) : BaseEntity() {

    init {
        this.content = content
        this.post = post
    }
    
    fun modify(content: String) {
        this.content = content
    }
}
