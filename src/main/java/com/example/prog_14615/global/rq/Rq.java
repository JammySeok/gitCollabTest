package com.example.prog_14615.global.rq;

import com.example.prog_14615.domain.post.member.AuthTokenService;
import com.example.prog_14615.domain.post.member.Member;
import com.example.prog_14615.domain.post.member.MemberService;
import com.example.prog_14615.global.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

// 인증 로직의 중복코드 때문에
@Component
@RequiredArgsConstructor
public class Rq {
    private final AuthTokenService authTokenService;
    private final MemberService memberService;
    private final HttpServletRequest request;

    // actor => 활동하고 있는 회원
    public Member getActor() {
        String apiKey;
        String accessToken;

        String headerAuthorization = getHeader("Authorization", "");

        if (!headerAuthorization.startsWith("Bearer "))
            throw new ServiceException("401-2", "Authorization 헤더가 Bearer 형식이 아닙니다.");

        String[] headerAuthorizationBits = headerAuthorization.split(" ", 3);

        apiKey = headerAuthorizationBits[1];
        accessToken = headerAuthorizationBits.length == 3 ? headerAuthorizationBits[2] : "";

        if (apiKey.isBlank())
            throw new ServiceException("401-1", "로그인 후 이용해주세요.");

        Member member = null;

        if (!accessToken.isBlank()) {
            Map<String, Object> payload = authTokenService.payload(accessToken);

            if (payload != null) {
                long id = (long) payload.get("id");
                member = memberService.findById(id)
                        .orElseThrow(() -> new ServiceException("401-4", "accessToken의 id에 해당하는 회원이 존재하지 않습니다."));
            }
        }

        if (member == null) {
            member = memberService
                    .findByApiKey(apiKey)
                    .orElseThrow(() -> new ServiceException("401-3", "API 키가 유효하지 않습니다."));
        }

        return member;
    }

    private String getHeader(String name, String defaultValue) {
        return Optional
                .ofNullable(request.getHeader(name))
                .filter(headerValue -> !headerValue.isBlank())
                .orElse(defaultValue);
    }
}