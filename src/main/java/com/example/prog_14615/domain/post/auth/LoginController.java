package com.example.prog_14615.domain.post.auth;

import com.example.prog_14615.domain.post.member.AuthTokenService;
import com.example.prog_14615.domain.post.member.Member;
import com.example.prog_14615.domain.post.member.MemberService;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final MemberService memberService;
    private final AuthTokenService authTokenService;

    @Getter
    @NoArgsConstructor
    static class LoginReqBody {
        String username;
        String password;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }

    @PostMapping("/v1/login")
    @ResponseBody
    public Map<String, Object> loginV1(@RequestBody LoginReqBody loginReqBody) {

        Optional<Member> opMember = memberService.findByUsername(loginReqBody.username);

        Map<String, Object> response = new HashMap<>();

        if(opMember.isEmpty()) {
            response.put("resultCode", "401");
            response.put("msg", "회원을 찾을 수 없습니다.");
            return response;
        }

        Member member = opMember.get();
        if(!member.getPassword().equals(loginReqBody.password)) {
            response.put("resultCode", "401");
            response.put("msg", "아이디 혹은 비밀번호가 다릅니다.");
            return response;
        }

        response.put("resultCode", "200");
        response.put("data", member.getUsername());

        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginReqBody loginReqBody) {

        Optional<Member> opMember = memberService.findByUsername(loginReqBody.username);
        Map<String, Object> response = new HashMap<>();

        if (opMember.isEmpty()) {
            response.put("resultCode", "401");
            response.put("msg", "회원을 찾을 수 없습니다.");

            return response;
        }

        Member member = opMember.get();
        if (!member.getPassword().equals(loginReqBody.password)) {
            response.put("resultCode", "401");
            response.put("msg", "비밀번호가 다릅니다.");

            return response;
        }

        response.put("resultCode", "200");
        response.put("msg", "로그인에 성공했습니다.");
        response.put("nickname", member.getNickname());
        response.put("data", authTokenService.genAccessToken(member));

        return response;
    }
}
