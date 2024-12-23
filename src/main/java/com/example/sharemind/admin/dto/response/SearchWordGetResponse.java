package com.example.sharemind.admin.dto.response;

import com.example.sharemind.searchWord.domain.SearchWord;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchWordGetResponse {

    @Schema(description = "검색어 아이디")
    private final Long wordId;

    @Schema(description = "검색어")
    private final String word;

    @Schema(description = "검색 횟수")
    private final Long count;

    @Schema(description = "최초 검색 일시")
    private final LocalDateTime createdAt;

    @Schema(description = "마지막 검색 일시")
    private final LocalDateTime updatedAt;

    @Builder
    public SearchWordGetResponse(Long wordId, String word, Long count, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.wordId = wordId;
        this.word = word;
        this.count = count;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SearchWordGetResponse of(SearchWord searchWord) {
        return SearchWordGetResponse.builder()
                .wordId(searchWord.getWordId())
                .word(searchWord.getWord())
                .count(searchWord.getCount())
                .createdAt(searchWord.getCreatedAt())
                .updatedAt(searchWord.getUpdatedAt())
                .build();
    }
}
