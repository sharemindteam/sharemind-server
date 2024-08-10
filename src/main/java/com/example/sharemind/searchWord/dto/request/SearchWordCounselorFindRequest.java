package com.example.sharemind.searchWord.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SearchWordCounselorFindRequest {

    @Schema(description = "검색어")
    @Size(min = 2, max = 20, message = "검색어는 2-20글자 사이에서 입력 가능합니다.")
    private String word;

    @Schema(description = "페이지 번호 index 0이면 0~3번째 값 반환, 1이면 4~7번째 값 반환", example = "0")
    @NotNull
    private int index;
}
