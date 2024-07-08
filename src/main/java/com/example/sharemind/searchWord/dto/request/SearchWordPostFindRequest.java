package com.example.sharemind.searchWord.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SearchWordPostFindRequest {

    @Schema(description = "검색어")
    @Size(min = 2, max = 20, message = "검색어는 2-20글자 사이에서 입력 가능합니다.")
    private String word;

    @Schema(description = "가장 마지막으로 받은 postId 반환, 처음 요청이라면 0을 반환", example = "0")
    private String postId;
}
