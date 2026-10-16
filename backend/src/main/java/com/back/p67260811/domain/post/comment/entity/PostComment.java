package com.back.p67260811.domain.post.comment.entity;

import com.back.p67260811.domain.post.post.entity.Post;
import com.back.p67260811.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PostComment extends BaseEntity {
    private String content;

    public PostComment(String comment, Post post) {
        this.content = content;
        this.post = post;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public void modify(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Post getPost() {
        return post;
    }
}
