package com.example.prog_14615.domain.post.member;

import com.example.prog_14615.standard.ut.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private String secretPattern= "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";
    private long expireSeconds = 1000L * 60 * 60 * 24 * 365;

    // 회원에 대한 토큰을 발급하고 테스트
    public String genAccessToken(Member member) {

        return Ut.jwt.toString(
                secretPattern,
                expireSeconds,
                Map.of("id", member.getId(), "username", member.getUsername())
        );
    }

    public Map<String, Object> payload(String jwt) {
        Map<String, Object> payload = Ut.jwt.payload(jwt, secretPattern);

        if(payload == null) {
            return null;
        }

        int id = (int)payload.get("id");
        String username = (String)payload.get("username");


        return Map.of("id", (long)id, "username", username);
    }
}