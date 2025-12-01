package com.example.prog_14615.domain.post.post;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RsData<T> {

    private String resultCode;
    private String message;

    // 어떻게 되었는지 결과, 부연설명, 필요한 데이터 등...
    private T data;
}
