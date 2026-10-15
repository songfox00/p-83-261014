package com.back.p67260811.domain.post.post.controller;

import com.back.p67260811.domain.member.entity.Member;
import com.back.p67260811.domain.member.service.MemberService;
import com.back.p67260811.domain.post.post.dto.PostDto;
import com.back.p67260811.domain.post.post.entity.Post;
import com.back.p67260811.domain.post.post.service.PostService;
import com.back.p67260811.global.dto.RsData;
import com.back.p67260811.global.rq.Rq;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PostDto> list() {
        List<Post> postList = postService.findAll();

        List<PostDto> postDtoList = postList.stream()
                .map(PostDto::new)
                .toList();

        return postDtoList;
    }

    @GetMapping("/{id}")
    public PostDto detail(
            @PathVariable int id
    ) {

        Post post = postService.findById(id).get();


        return new PostDto(post);
    }


    record PostWriteReqBody(
            @Size(min = 2, max = 10, message = "제목은 2글자 이상 10글자 이하로 작성해주세요.")
            @NotBlank(message = "제목을 입력해주세요.")
            String title,

            @Size(min = 2, max = 10, message = "내용은 2글자 이상 10글자 이하로 작성해주세요.")
            @NotBlank(message = "내용을 입력해주세요.")
            String content
    ) {
    }

    @PostMapping
    @Transactional
    public RsData<PostDto> write(
            @Valid @RequestBody PostWriteReqBody reqBody
    ) {

        Member actor = rq.getActor();

        Post post = postService.write(actor, reqBody.title, reqBody.content);
        return new RsData<>(
                "201-1",
                "%d번 글이 성공적으로 등록되었습니다".formatted(post.getId()),
                new PostDto(post)
        );
    }


    record PostModifyReqBody(
            @Size(min = 2, max = 10, message = "제목은 2글자 이상 10글자 이하로 작성해주세요.")
            @NotBlank(message = "제목을 입력해주세요.")
            String title,
            @Size(min = 2, max = 10, message = "내용은 2글자 이상 10글자 이하로 작성해주세요.")
            @NotBlank(message = "내용을 입력해주세요.")
            String content
    ) {
    }

    @PatchMapping("/{id}")
    @Transactional
    public RsData<Void> modify(
            @PathVariable int id,
            @Valid @RequestBody PostModifyReqBody reqBody
    ) {

        Member actor = rq.getActor();
        Post post = postService.findById(id).get();
        post.checkActorModify(actor);

        postService.modify(post, reqBody.title, reqBody.content);

        return new RsData<>(
                "200-1",
                "%d번 게시물이 수정되었습니다.".formatted(id)
        );
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(
            @PathVariable int id
    ) {

        Member actor = rq.getActor();
        Post post = postService.findById(id).get();
        post.checkActorDelete(actor);

        postService.delete(id);

        return new RsData<>(
                "200-1",
                "%d번 게시물이 삭제되었습니다.".formatted(id)
        );
    }

    // 인증 처리 메서드
}