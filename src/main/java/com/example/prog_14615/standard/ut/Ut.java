package com.example.prog_14615.standard.ut;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

// Util
public class Ut {

    // jwt 관련 유틸
    public static class jwt {
        public static String toString(String secret, long expireSeconds, Map<String, Object> body) {
            ClaimsBuilder claimsBuilder = Jwts.claims();

            for (Map.Entry<String, Object> entry : body.entrySet()) {
                claimsBuilder.add(entry.getKey(), entry.getValue());
            }

            Claims claims = claimsBuilder.build();

            Date issuedAt = new Date();
            Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

            Key secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            String jwt = Jwts.builder()
                    .claims(claims)
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(secretKey)
                    .compact();

            return jwt;
        }

        // jwt 토큰에서 정보를 추출하기 위한 유틸 함수
        public static Map<String, Object> payload(String jwt, String secretPattern) {

            SecretKey secretKey = Keys.hmacShaKeyFor(secretPattern.getBytes(StandardCharsets.UTF_8));

            return  (Map<String, Object>) Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(jwt)
                    .getPayload();
        }
    }

    // 숫자 관련 유틸
    public static class str {
    }
}