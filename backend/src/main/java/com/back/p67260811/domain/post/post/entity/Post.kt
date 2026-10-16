package com.back.p67260811.domain.post.post.entity

import com.back.p67260811.domain.member.entity.Member
import com.back.p67260811.domain.post.comment.entity.PostComment
import com.back.p67260811.global.exception.ServiceException
import com.back.p67260811.global.jpa.entity.BaseEntity
import jakarta.persistence.*
import java.util.function.Supplier

@Entity
class Post(// 외래키인 id만 사용
    @field:ManyToOne(fetch = FetchType.LAZY)
    var author: Member,
    var title: String,
    var content: String,
) : BaseEntity() {

    @OneToMany(
        mappedBy = "post",
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    private var comments: MutableList<PostComment> = mutableListOf()

    fun findCommentById(id: Int): PostComment {
        return comments
            .stream()
            .filter { c: PostComment -> c.id == id }
            .findFirst()
            .orElseThrow(Supplier { IllegalArgumentException("해당 댓글이 존재하지 않습니다") })
    }

    fun addComment(content: String): PostComment {
        val postComment = PostComment(content, this)
        this.comments.add(postComment)

        return postComment
    }

    fun removeComment(id: Int) {
        comments.removeIf { comment: PostComment -> comment.id == id }
    }

    fun modifyComment(id: Int, content: String): PostComment {
        val postComment = comments
            .stream()
            .filter { comment: PostComment -> comment.id == id }
            .findFirst()
            .orElseThrow<IllegalArgumentException?>(Supplier { IllegalArgumentException("해당 댓글이 존재하지 않습니다") })

        postComment.modify(content)

        return postComment
    }

    fun update(title: String, content: String) {
        // 비즈니스 규칙

        this.title = title
        this.content = content
    }

    fun checkActorModify(actor: Member) {
        if (author.id !== actor.id) {
            throw ServiceException("403-1", "수정 권한이 없습니다.")
        }
    }

    fun checkActorDelete(actor: Member) {
        if (author.id !== actor.id) {
            throw ServiceException("403-2", "삭제 권한이 없습니다.")
        }
    }

    fun getComments(): List<PostComment> = comments
}