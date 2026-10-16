package com.back.p67260811.domain.post.post.repository

import com.back.p67260811.domain.post.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Int>