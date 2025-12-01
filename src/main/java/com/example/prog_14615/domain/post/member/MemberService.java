package com.example.prog_14615.domain.post.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long count() {
        return memberRepository.count();
    }

    public void save (List<String> list) {
        for (String n : list) {
            Member m = new Member();
            m.setNickname(n);
            memberRepository.save(m);
        }
    }

    public List<Member> getList() {
        return memberRepository.findAll();
    }


    public void join(String username, String password, String nickname) {

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);
        member.setNickname(nickname);
        member.setApiKey(UUID.randomUUID().toString());  // 무작위 key 값 뽑기

        memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findByApiKey(String apiKey) {
        return memberRepository.findByApiKey(apiKey);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}
