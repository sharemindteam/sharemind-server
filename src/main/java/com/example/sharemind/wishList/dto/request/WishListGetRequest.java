package com.example.sharemind.wishList.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class WishListGetRequest {

    @NotNull(message = "찜하기 id는 공백일 수 없습니다.")
    @Schema(description = "찜하기 id", example = "1")
    Long wishlistId;

    @Schema(description = "찜하기가 업데이트된 시간", example = "2024-01-25T15:15:35")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt;
}
