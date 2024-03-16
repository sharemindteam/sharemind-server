package com.example.sharemind.post.dto.response;

import com.example.sharemind.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostGetIsSavedResponse {

    @Schema(description = "임시저장 메시지 존재하면 true, 아니면 false", example = "true")
    private final Boolean isSaved;

    @Schema(description = "마지막 수정일시, isSaved false면 null", example = "2023년 12월 25일 오후 12시 34분", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년 MM월 dd일 a HH시 mm분")
    private final LocalDateTime updatedAt;

    @Builder
    public PostGetIsSavedResponse(Boolean isSaved, LocalDateTime updatedAt) {
        this.isSaved = isSaved;
        this.updatedAt = updatedAt;
    }

    public static PostGetIsSavedResponse of(Post post) {
        return PostGetIsSavedResponse.builder()
                .isSaved(true)
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostGetIsSavedResponse of() {
        return PostGetIsSavedResponse.builder().build();
    }
}
