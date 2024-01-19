package com.example.sharemind.searchWord.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class searchWordFindRequest {

    @Schema(description = "닉네임")
    @Size(min = 2, max = 10, message = "검색어는 2-20글자 사이에서 입력 가능합니다.")
    private String word;

    @Schema(description = "받은 메세지 개수", example = "0")
    @NotNull
    private Long index;
}
