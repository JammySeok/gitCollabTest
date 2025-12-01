package com.example.prog_14615.domain.post.member;

import com.example.prog_14615.domain.post.post.RsData;
import com.example.prog_14615.global.exception.ServiceException;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ApiV1MemberController {

    private final MemberService memberService;
    private final AuthTokenService authTokenService;

    @Getter
    @NoArgsConstructor
    static class LoginReqBody {
        String username;
        String password;
    }

    @Getter
    @AllArgsConstructor
    static class LoginResBody {
        private String apiKey;
        private String accessToken;
    }

    @PostMapping("/api/v1/login")
    @ResponseBody
    public RsData login(@RequestBody LoginReqBody loginReqBody) {
        Optional<Member> opMember = memberService.findByUsername(loginReqBody.username);

        // 우리 회원이 맞는지 체크
        if(opMember.isEmpty()) {
//            return "없는 회원입니다.";  // 인증 실패(401)
            throw new ServiceException("401", "없는 회원입니다.");
        }

        Member member = opMember.get();
        if(!member.getPassword().equals(loginReqBody.password)) {
            throw new ServiceException("401", "비밀번호를 틀렸습니다.");
        }

        // 로그인 성공하면 apiKey와 함께 accessToken 발급
        String apiKey = member.getApiKey();
        String accessToken = authTokenService.genAccessToken(member);

        LoginResBody resBody = new LoginResBody(apiKey, accessToken);

        RsData rsData = new RsData();
        rsData.setResultCode("200");
        rsData.setMessage(loginReqBody.username + "님 로그인 하였습니다.");
        rsData.setData(resBody);

        return rsData;
    }


    @GetMapping("/api/v1/members")
    @ResponseBody
    public List<Member> list() {
        List<Member> members = memberService.getList();
        return members;
    }
}
