package com.example.prog_14615.domain.post.post;

import com.example.prog_14615.domain.post.member.Member;
import com.example.prog_14615.domain.post.member.MemberService;
import com.example.prog_14615.global.exception.ServiceException;
import com.example.prog_14615.global.rq.Rq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ApiV1PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @Getter
    @NoArgsConstructor
    static class WriteReqBody {
        private String title;
        private String content;
    }

    @ResponseBody
    @PostMapping("/api/v1/posts")
    public RsData writeV1(@RequestBody WriteReqBody writeReqBody, @RequestHeader("Authorization") String apiKey) {

        // apiKey 보낼 때 앞에 Bearer를 붙이는게 관례
        if(apiKey.startsWith("Bearer ")) {
            apiKey = apiKey.replace("Bearer ", "");
        }
        Optional<Member> opMember = memberService.findByApiKey(apiKey);  // Bearer

        // 우리 회원이 맞는지 체크
        if(opMember.isEmpty()) {
//            return "없는 회원입니다.";  // 인증 실패(401)
            throw new ServiceException("401", "잘못된 api 키 입니다.");
        }

        postService.write(writeReqBody.title, writeReqBody.content);

        Member member = opMember.get();

        RsData rsData = new RsData();
        rsData.setResultCode("200");
        rsData.setMessage(member.getApiKey() + " (작성 성공)");
        return rsData;
    }

    @ResponseBody
    @PostMapping("/api/v2/posts")
    public RsData writeV2(@RequestBody WriteReqBody writeReqBody) {
            Member member = rq.getActor();
            postService.write(writeReqBody.title, writeReqBody.content);

            RsData rsData = new RsData();
            rsData.setResultCode("200");
            rsData.setMessage(member.getUsername() + "님의 글 작성이 완료되었습니다.");

            return rsData;
    }

    @ResponseBody
    @GetMapping("/api/v1/posts/{id}")
    public Post getPostById(@PathVariable("id") Long id) {
        return postService.getIdList(id);
    }

    @ResponseBody
    @GetMapping("api/v1/posts")
    public List<Post> getPost() {
        return postService.getList();
    }

    @ResponseBody
    @DeleteMapping("/api/v1/posts/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.delete(id);
        return "삭제 되었습니다.";
    }

//    @ResponseBody
//    @PatchMapping("api/v1/posts/{id}/")
//    public RsData modifyContent(@PathVariable Long id, @RequestBody Post modifiedPost, String username, String password) {
//        Optional<Member> opMember = memberService.findByUsername(username);
//
//        if(opMember.isEmpty()) {
//            return new RsData("401", "인증실패: 회원을 찾을 수 없음");
//        }
//
//        Member member = opMember.get();
//        if(!member.getPassword().equals(password)) {
//            return new RsData("401", "인증실패: 비밀번호가 일치하지 않음");  // 인증 실패(401)
//        }
//
//        postService.modify(id, modifiedPost.getContent());
//
//        return new RsData("200", "게시글 수정이 완료되었습니다.");
//    }

}
